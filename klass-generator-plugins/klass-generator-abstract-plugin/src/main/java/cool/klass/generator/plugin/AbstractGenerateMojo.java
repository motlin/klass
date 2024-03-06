package cool.klass.generator.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.set.mutable.SetAdapter;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public abstract class AbstractGenerateMojo extends AbstractMojo
{
    @Parameter(property = "klassSourcePackages", required = true, readonly = true)
    protected List<String> klassSourcePackages;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject mavenProject;

    @Nonnull
    protected DomainModel getDomainModel() throws MojoExecutionException
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

        ImmutableList<String> klassSourcePackagesImmutable = Lists.immutable.withAll(this.klassSourcePackages);
        this.getLog().debug("Scanning source packages: " + klassSourcePackagesImmutable.makeString());

        ClassLoader classLoader = this.getClassLoader();

        ImmutableList<URL> urls = klassSourcePackagesImmutable.flatCollectWith(
                ClasspathHelper::forPackage,
                classLoader);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList());
        Reflections reflections    = new Reflections(configurationBuilder);
        Set<String> klassLocations = reflections.getResources(Pattern.compile(".*\\.klass"));
        this.getLog().debug("Found klass locations: " + SetAdapter.adapt(klassLocations).makeString());

        MutableList<CompilationUnit> compilationUnits = Lists.mutable.withAll(klassLocations)
                .collectWith(CompilationUnit::createFromClasspathLocation, classLoader);

        CompilerState compilerState = new CompilerState(compilationUnits);
        KlassCompiler klassCompiler = new KlassCompiler(compilerState);
        DomainModel   domainModel   = klassCompiler.compile();

        ImmutableList<RootCompilerError> compilerErrors = compilerState.getCompilerErrors();
        if (compilerErrors.notEmpty())
        {
            for (RootCompilerError compilerError : compilerErrors)
            {
                this.getLog().warn(compilerError.toString());
            }
            throw new MojoExecutionException("There were compiler errors.");
        }

        return domainModel;
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
                URL url = this.getUrl(element);
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
    private URL getUrl(@Nonnull String classpathElement) throws MojoExecutionException
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
