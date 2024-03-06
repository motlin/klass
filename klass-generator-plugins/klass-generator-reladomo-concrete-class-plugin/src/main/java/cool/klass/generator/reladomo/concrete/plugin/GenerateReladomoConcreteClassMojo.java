package cool.klass.generator.reladomo.concrete.plugin;

import java.io.File;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.generator.reladomo.concrete.ReladomoConcreteClassGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-reladomo-concrete-classes",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateReladomoConcreteClassMojo
        extends AbstractGenerateMojo
{
    @Parameter(
            property = "generatedDir",
               defaultValue = "${project.build.directory}/generated-sources/reladomo-concrete-classes")
    private File generatedDir;

    @Override
    public void execute() throws MojoExecutionException
    {
        DomainModel domainModel = this.getDomainModel();

        ReladomoConcreteClassGenerator generator = new ReladomoConcreteClassGenerator(domainModel);
        try
        {
            generator.writeConcreteClasses(this.generatedDir.toPath());
        }
        catch (RuntimeException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
