package cool.klass.generator.meta.constants.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

import cool.klass.generator.meta.constants.JavaConstantsMetaModelGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "generate-meta-model-constants", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class GenerateJavaConstantsMetaModelMojo extends AbstractGenerateMojo
{
    @Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/meta-model-constants")
    private File outputDirectory;

    @Parameter(property = "applicationName", required = true, readonly = true)
    private String applicationName;

    @Parameter(property = "rootPackageName", required = true, readonly = true)
    private String rootPackageName;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject mavenProject;

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
            JavaConstantsMetaModelGenerator javaConstantsMetaModelGenerator = new JavaConstantsMetaModelGenerator(
                    domainModel,
                    this.applicationName,
                    this.rootPackageName,
                    Instant.now());
            javaConstantsMetaModelGenerator.writeJavaConstantsMetaModelFiles(outputPath);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
