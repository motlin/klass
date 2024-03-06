package cool.klass.generator.service.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.generator.service.ServiceResourceGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-service-resources",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateServiceResourcesMojo extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/service-resources")
    private File outputDirectory;

    @Parameter(property = "applicationName", required = true, readonly = true)
    private String applicationName;

    @Parameter(property = "rootPackageName", required = true, readonly = true)
    private String rootPackageName;

    @Override
    public void execute() throws MojoExecutionException
    {
        if (!this.outputDirectory.exists())
        {
            this.outputDirectory.mkdirs();
        }

        DomainModel domainModel = this.getDomainModel();
        Path        outputPath  = this.outputDirectory.toPath();
        try
        {
            ServiceResourceGenerator serviceResourceGenerator = new ServiceResourceGenerator(
                    domainModel,
                    this.applicationName,
                    this.rootPackageName,
                    Instant.now());
            serviceResourceGenerator.writeServiceResourceFiles(outputPath);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
