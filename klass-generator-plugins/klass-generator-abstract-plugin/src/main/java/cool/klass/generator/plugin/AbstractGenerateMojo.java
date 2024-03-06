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
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.AbstractCompilerAnnotation;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.DomainModel;
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
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public abstract class AbstractGenerateMojo
        extends AbstractMojo
{
    public static final Pattern KLASS_FILE_EXTENSION = Pattern.compile(".*\\.klass");
    @Parameter(property = "klassSourcePackages", required = true)
    protected List<String> klassSourcePackages;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject mavenProject;

    @Nonnull
    protected DomainModel getDomainModelFromFiles() throws MojoExecutionException
    {
        CompilationResult compilationResult = this.getCompilationResultFromFiles();

        this.handleErrorsCompilationResult(compilationResult);

        if (compilationResult instanceof DomainModelCompilationResult)
        {
            return ((DomainModelCompilationResult) compilationResult).domainModel();
        }

        throw new AssertionError(compilationResult.getClass().getSimpleName());
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

    @Nonnull
    protected DomainModel getDomainModel() throws MojoExecutionException
    {
        // TODO: We should use an abstract DomainModelFactory here, not necessarily the compiler.
        CompilationResult compilationResult = this.getCompilationResult();

        this.handleErrorsCompilationResult(compilationResult);

        if (compilationResult instanceof DomainModelCompilationResult)
        {
            return ((DomainModelCompilationResult) compilationResult).domainModel();
        }

        throw new AssertionError(compilationResult.getClass().getSimpleName());
    }

    protected void handleErrorsCompilationResult(CompilationResult compilationResult) throws MojoExecutionException
    {
        if (compilationResult instanceof ErrorsCompilationResult errorsCompilationResult)
        {
            ImmutableList<RootCompilerAnnotation> compilerAnnotations = errorsCompilationResult.compilerAnnotations();
            ImmutableList<RootCompilerAnnotation> errors = compilerAnnotations.select(AbstractCompilerAnnotation::isError);
            ImmutableList<RootCompilerAnnotation> warnings = compilerAnnotations.select(AbstractCompilerAnnotation::isWarning);

            for (RootCompilerAnnotation error : errors)
            {
                this.getLog().info(error.toGitHubAnnotation());
                this.getLog().error(error.toString());
            }
            for (RootCompilerAnnotation warning : warnings)
            {
                this.getLog().info(warning.toGitHubAnnotation());
                this.getLog().warn(warning.toString());
            }

            if (errors.notEmpty())
            {
                throw new MojoExecutionException("There were compiler errors.");
            }
        }
    }

    protected CompilationResult getCompilationResult() throws MojoExecutionException
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

        ClassLoader classLoader = this.getClassLoader();

        return this.load(classLoader);
    }

    @Nonnull
    private CompilationResult load(ClassLoader classLoader) throws MojoExecutionException
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.withAll(this.klassSourcePackages);
        this.getLog().debug("Scanning source packages: " + klassSourcePackages.makeString());

        ImmutableList<CompilationUnit> compilationUnits = this.getCompilationUnits(
                classLoader,
                klassSourcePackages);

        KlassCompiler klassCompiler = new KlassCompiler(compilationUnits);
        return klassCompiler.compile();
    }

    @Nonnull
    private ImmutableList<CompilationUnit> getCompilationUnits(
            ClassLoader classLoader,
            ImmutableList<String> klassSourcePackages) throws MojoExecutionException
    {
        ImmutableList<URL> urls = klassSourcePackages.flatCollectWith(
                ClasspathHelper::forPackage,
                classLoader);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList());
        Reflections reflections    = new Reflections(configurationBuilder);
        ImmutableList<String> klassLocations = Lists.immutable.withAll(reflections.getResources(KLASS_FILE_EXTENSION));

        this.getLog().debug("Found source files on classpath: " + klassLocations);

        ImmutableList<CompilationUnit> compilationUnits = Lists.immutable.withAll(klassLocations)
                .collectWithIndex((each, index) -> CompilationUnit.createFromClasspathLocation(index, each, classLoader));

        if (compilationUnits.isEmpty())
        {
            String message = "Could not find any files matching *.klass in urls: " + urls;
            throw new MojoExecutionException(message);
        }
        return compilationUnits;
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
