package cool.klass.generator.plugin;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.MutableSet;
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

    @Nullable
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

        ImmutableList<URL> urls = klassSourcePackagesImmutable.flatCollect(ClasspathHelper::forPackage);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList());
        Reflections reflections    = new Reflections(configurationBuilder);
        Set<String> klassLocations = reflections.getResources(Pattern.compile(".*\\.klass"));

        MutableSet<CompilationUnit> compilationUnits = SetAdapter.adapt(klassLocations)
                .collect(CompilationUnit::createFromClasspathLocation);

        CompilerState compilerState = new CompilerState(compilationUnits);
        KlassCompiler klassCompiler = new KlassCompiler(compilerState);
        DomainModel   domainModel   = klassCompiler.compile();

        ImmutableList<CompilerError> compilerErrors = compilerState.getCompilerErrors();
        if (compilerErrors.notEmpty())
        {
            for (CompilerError compilerError : compilerErrors)
            {
                this.getLog().warn(compilerError.toString());
            }
            throw new MojoExecutionException("There were compiler errors.");
        }

        return domainModel;
    }
}
