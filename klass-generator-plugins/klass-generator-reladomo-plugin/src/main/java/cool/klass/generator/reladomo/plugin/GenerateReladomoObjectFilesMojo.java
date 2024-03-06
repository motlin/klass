package cool.klass.generator.reladomo.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.generator.reladomo.ReladomoObjectFileGenerator;
import cool.klass.model.meta.domain.DomainModel;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate-reladomo-object-files", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GenerateReladomoObjectFilesMojo extends AbstractGenerateMojo
{
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-resources/reladomo")
    private File outputDirectory;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException
    {
        if (!this.outputDirectory.exists())
        {
            this.outputDirectory.mkdirs();
        }

        Path        outputPath  = this.outputDirectory.toPath();
        DomainModel domainModel = this.getDomainModel();

        ReladomoObjectFileGenerator reladomoObjectFileGenerator = new ReladomoObjectFileGenerator(domainModel);
        try
        {
            reladomoObjectFileGenerator.writeObjectFiles(outputPath);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        Resource resource = new Resource();
        resource.setDirectory(this.outputDirectory.getAbsolutePath());
        // TODO: Should be based on the output path
        resource.setTargetPath("reladomo");
        this.mavenProject.addResource(resource);
    }
}
