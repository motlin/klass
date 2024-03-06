package cool.klass.generator.reladomo.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.generator.reladomo.ReladomoRuntimeConfigurationGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(name = "generate-reladomo-runtime-configuration", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateReladomoRuntimeConfigurationMojo extends AbstractGenerateMojo
{
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-test-resources/reladomo-runtime-configuration")
    private File outputDirectory;

    @Parameter(property = "outputFilename", required = true, readonly = true, defaultValue = "TestReladomoRuntimeConfiguration.xml")
    private String outputFilename;

    @Parameter(property = "connectionManagerFullyQualifiedName", required = true, readonly = true, defaultValue = "com.gs.fw.common.mithra.test.ConnectionManagerForTests")
    private String connectionManagerFullyQualifiedName;

    @SuppressWarnings("FieldMayBeFinal")
    @Parameter(property = "isTest", required = true, readonly = true, defaultValue = "true")
    private boolean isTest = true;

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

        Path outputPath = this.outputDirectory.toPath();
        Path path       = outputPath.resolve(this.outputFilename);
        try
        {
            ReladomoRuntimeConfigurationGenerator reladomoRuntimeConfigurationGenerator = new ReladomoRuntimeConfigurationGenerator(
                    domainModel,
                    this.connectionManagerFullyQualifiedName,
                    this.isTest,
                    this.rootPackageName);
            reladomoRuntimeConfigurationGenerator.writeRuntimeConfigFile(path);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        Resource resource = new Resource();
        resource.setDirectory(this.outputDirectory.getAbsolutePath());
        // TODO: Should be based on the output path
        resource.setTargetPath("reladomo-runtime-configuration");
        if (this.isTest)
        {
            this.mavenProject.addTestResource(resource);
        }
        else
        {
            this.mavenProject.addResource(resource);
        }
    }
}
