package cool.klass.generator.klass.inference.plugin;

import java.io.File;

import cool.klass.generator.klass.inference.KlassWithInferenceGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-klass-with-inference",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateKlassWithInferenceMojo extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/klass-with-inference")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException
    {
        DomainModel domainModel = this.getDomainModel();
        KlassWithInferenceGenerator generator = new KlassWithInferenceGenerator(domainModel);
        generator.writeKlassFiles(this.outputDirectory.toPath());
    }
}
