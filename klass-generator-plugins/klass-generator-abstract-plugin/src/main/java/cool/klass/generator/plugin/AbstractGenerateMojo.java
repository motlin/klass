package cool.klass.generator.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.fusesource.jansi.AnsiConsole;

public abstract class AbstractGenerateMojo
        extends AbstractMojo
{
    public static final Pattern KLASS_FILE_EXTENSION = Pattern.compile(".*\\.klass");
    @Parameter(property = "klassSourcePackages", required = true)
    protected List<String> klassSourcePackages;

    @Parameter(property = "logCompilerAnnotations")
    protected boolean logCompilerAnnotations;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject mavenProject;

    @Nonnull
    protected DomainModelWithSourceCode getDomainModelFromFiles() throws MojoExecutionException
    {
        CompilationResult compilationResult = this.getCompilationResultFromFiles();

        this.handleErrorsCompilationResult(compilationResult);

        return compilationResult.domainModelWithSourceCode().get();
    }

    @Nonnull
    private CompilationResult getCompilationResultFromFiles()
            throws MojoExecutionException
    {
        MutableList<File> klassLocations = this.loadFiles();

        if (klassLocations.isEmpty())
        {
            String message = "Could not find any files matching %s in: %s".formatted(
                    KLASS_FILE_EXTENSION,
                    this.mavenProject.getResources());
            throw new MojoExecutionException(message);
        }

        ImmutableList<CompilationUnit> compilationUnits = this.getCompilationUnits(klassLocations.toImmutable());

        // TODO: We should use an abstract DomainModelFactory here, not necessarily the compiler.
        KlassCompiler klassCompiler = new KlassCompiler(compilationUnits);
        return klassCompiler.compile();
    }

    @Nonnull
    protected DomainModelWithSourceCode getDomainModel() throws MojoExecutionException
    {
        if (this.klassSourcePackages.isEmpty())
        {
            String message = ""
                             + "Klass maven plugins must be configured with at least one klassSourcePackage. For example:\n"
                             + "<klassSourcePackages>\n"
                             + "    <klassSourcePackage>klass.model.meta.domain</klassSourcePackage>\n"
                             + "    <klassSourcePackage>${app.rootPackageName}</klassSourcePackage>\n"
                             + "</klassSourcePackages>";
            throw new MojoExecutionException(message);
        }

        // TODO: We should use an abstract DomainModelFactory here, not necessarily the compiler.
        var loader = new DomainModelCompilerLoader(
                Lists.immutable.withAll(this.klassSourcePackages),
                this.getClassLoader(),
                this::logCompilerAnnotation);
        return loader.load();
    }

    private MutableList<File> loadFiles()
    {
        MutableList<String> adaptedKlassSourcePackages = ListAdapter.adapt(this.klassSourcePackages);

        MutableList<File> klassLocations = Lists.mutable.empty();
        for (Resource resource : this.mavenProject.getResources())
        {
            this.loadfiles(klassLocations, adaptedKlassSourcePackages, resource);
        }
        return klassLocations;
    }

    private void loadfiles(
            MutableList<File> resultKlassLocations,
            ListIterable<String> klassSourcePackages,
            Resource resource)
    {
        String directory = resource.getDirectory();
        this.getLog().info("Scanning source packages: " + klassSourcePackages.makeString() + " in directory: " + directory);

        klassSourcePackages
                .asLazy()
                .collect(klassSourcePackage -> klassSourcePackage.replaceAll("\\.", "/"))
                .collect(relativeDirectory -> new File(directory, relativeDirectory))
                .forEach(file ->
                {
                    File[] files = file.listFiles();
                    if (files == null)
                    {
                        String message = "Could not find directory: " + file.getAbsolutePath();
                        this.getLog().warn(message);
                    }
                });

        // list all files in sourceDirectory
        klassSourcePackages
                .asLazy()
                .collect(klassSourcePackage -> klassSourcePackage.replaceAll("\\.", "/"))
                .collect(relativeDirectory -> new File(directory, relativeDirectory))
                .collect(File::listFiles)
                .reject(Objects::isNull)
                .collect(ArrayAdapter::adapt)
                .forEach(files -> files
                        .asLazy()
                        .select(file -> KLASS_FILE_EXTENSION.matcher(file.getAbsolutePath()).matches())
                        .into(resultKlassLocations));
    }

    protected void handleErrorsCompilationResult(CompilationResult compilationResult) throws MojoExecutionException
    {
        for (RootCompilerAnnotation compilerAnnotation : compilationResult.compilerAnnotations())
        {
            this.logCompilerAnnotation(compilerAnnotation);
        }

        if (compilationResult.domainModelWithSourceCode().isEmpty())
        {
            throw new MojoExecutionException("There were compiler errors.");
        }
    }

    private void logCompilerAnnotation(RootCompilerAnnotation compilerAnnotation)
    {
        AnsiConsole.systemInstall();

        if (compilerAnnotation.isError())
        {
            this.getLog().info(compilerAnnotation.toGitHubAnnotation());
            this.getLog().error("\n" + compilerAnnotation);
        }
        else if (compilerAnnotation.isWarning() && this.logCompilerAnnotations)
        {
            this.getLog().info(compilerAnnotation.toGitHubAnnotation());
            this.getLog().warn("\n" + compilerAnnotation);
        }
    }

    private ImmutableList<CompilationUnit> getCompilationUnits(ImmutableList<File> klassLocations)
    {
        this.getLog().debug("Found source files on classpath: " + klassLocations);

        ImmutableList<CompilationUnit> compilationUnits = klassLocations
                .collectWithIndex((each, index) -> CompilationUnit.createFromFile(index, each));

        return compilationUnits;
    }

    @Nonnull
    private ClassLoader getClassLoader() throws MojoExecutionException
    {
        try
        {
            List<String>     classpathElements    = this.mavenProject.getCompileClasspathElements();
            MutableList<URL> projectClasspathList = Lists.mutable.empty();
            for (String element : classpathElements)
            {
                URL url = AbstractGenerateMojo.getUrl(element);
                projectClasspathList.add(url);
            }

            URL[] urls = projectClasspathList.toArray(new URL[0]);
            return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        }
        catch (DependencyResolutionRequiredException e)
        {
            throw new MojoExecutionException("Dependency resolution failed", e);
        }
    }

    @Nonnull
    private static URL getUrl(@Nonnull String classpathElement) throws MojoExecutionException
    {
        try
        {
            return new File(classpathElement).toURI().toURL();
        }
        catch (MalformedURLException e)
        {
            throw new MojoExecutionException(classpathElement + " is an invalid classpath element", e);
        }
    }
}
