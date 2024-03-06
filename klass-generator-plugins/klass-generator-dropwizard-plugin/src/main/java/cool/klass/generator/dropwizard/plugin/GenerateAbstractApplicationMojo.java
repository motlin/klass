package cool.klass.generator.dropwizard.plugin;

import java.io.File;
import java.io.IOException;

import cool.klass.generator.dropwizard.AbstractApplicationGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.meta.domain.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate-abstract-application", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GenerateAbstractApplicationMojo extends AbstractGenerateMojo
{
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/service-resources")
    private File outputDirectory;

    @Parameter(property = "applicationName", required = true, readonly = true)
    private String applicationName;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoExecutionException
    {
        DomainModel domainModel = this.getDomainModel();
        try
        {
            AbstractApplicationGenerator abstractApplicationGenerator = new AbstractApplicationGenerator(
                    domainModel,
                    this.rootPackageName,
                    this.applicationName);
            abstractApplicationGenerator.writeAbstractApplicationFile(this.outputDirectory.toPath());
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
