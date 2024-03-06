package cool.klass.generator.reladomo.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.generator.reladomo.ReladomoClassListGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "generate-reladomo-class-list", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateReladomoClassListMojo extends AbstractGenerateMojo
{
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-resources/reladomo")
    private File outputDirectory;

    @Parameter(property = "outputFilename", required = true, readonly = true, defaultValue = "ReladomoClassList.xml")
    private String outputFilename;

    @Override
    public void execute() throws MojoExecutionException
    {
        if (!this.outputDirectory.exists())
        {
            this.outputDirectory.mkdirs();
        }

        DomainModel domainModel = this.getDomainModel();

        Path outputPath = this.outputDirectory.toPath();
        Path path       = outputPath.resolve(this.outputFilename);
        try
        {
            ReladomoClassListGenerator reladomoClassListGenerator = new ReladomoClassListGenerator(domainModel);
            reladomoClassListGenerator.writeClassListFile(path);
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
