package cool.klass.generator.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public abstract class AbstractGenerateMojo
        extends AbstractMojo
{
    @Parameter(property = "klassSourcePackages", required = true, readonly = true)
    protected List<String> klassSourcePackages;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject mavenProject;

    @Nonnull
    protected DomainModel getDomainModel() throws MojoExecutionException
    {
        // TODO: We should use an abstract DomainModelFactory here, not necessarily the compiler.
        CompilationResult compilationResult = this.getCompilationResult();

        this.handleErrorsCompilationResult(compilationResult);

        if (compilationResult instanceof DomainModelCompilationResult)
        {
            return ((DomainModelCompilationResult) compilationResult).getDomainModel();
        }

        throw new AssertionError(compilationResult.getClass().getSimpleName());
    }

    protected void handleErrorsCompilationResult(CompilationResult compilationResult) throws MojoExecutionException
    {
        if (compilationResult instanceof ErrorsCompilationResult)
        {
            ErrorsCompilationResult          errorsCompilationResult = (ErrorsCompilationResult) compilationResult;
            ImmutableList<RootCompilerError> compilerErrors          = errorsCompilationResult.getCompilerErrors();
            for (RootCompilerError compilerError : compilerErrors)
            {
                this.getLog().warn(compilerError.toString());
            }
            throw new MojoExecutionException("There were compiler errors.");
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

        ImmutableCollection<CompilationUnit> compilationUnits = this.getCompilationUnits(
                classLoader,
                klassSourcePackages);

        KlassCompiler klassCompiler = new KlassCompiler(compilationUnits);
        return klassCompiler.compile();
    }

    @Nonnull
    private ImmutableCollection<CompilationUnit> getCompilationUnits(
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
        ImmutableList<String> klassLocations = Lists.immutable.withAll(reflections.getResources(Pattern.compile(".*\\.klass")));

        this.getLog().debug("Found source files on classpath: " + klassLocations);

        ImmutableCollection<CompilationUnit> compilationUnits = Lists.immutable.withAll(klassLocations)
                .collectWith(CompilationUnit::createFromClasspathLocation, classLoader);

        if (compilationUnits.isEmpty())
        {
            String message = "Could not find any files matching *.klass in urls: " + urls;
            throw new MojoExecutionException(message);
        }
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
