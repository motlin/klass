package cool.klass.generator.reladomo.plugin;

import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.collections.api.list.ImmutableList;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public abstract class AbstractGenerateReladomoMojo extends AbstractMojo
{
    @Parameter(property = "rootPackageName", required = true, readonly = true)
    private String rootPackageName;

    @Nullable
    protected DomainModel getDomainModel() throws MojoExecutionException
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new ResourcesScanner())
                .setUrls(ClasspathHelper.forPackage(this.rootPackageName)));
        Set<String> klassLocations =
                reflections.getResources(Pattern.compile(".*\\.klass"));
        CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
        KlassCompiler       klassCompiler       = new KlassCompiler(compilerErrorHolder);
        DomainModel         domainModel         = klassCompiler.compile(klassLocations);

        if (compilerErrorHolder.hasCompilerErrors())
        {
            ImmutableList<CompilerError> compilerErrors = compilerErrorHolder.getCompilerErrors();
            for (CompilerError compilerError : compilerErrors)
            {
                this.getLog().warn(compilerError.toString());
            }
            throw new MojoExecutionException("There were compiler errors.");
        }

        return domainModel;
    }
}
