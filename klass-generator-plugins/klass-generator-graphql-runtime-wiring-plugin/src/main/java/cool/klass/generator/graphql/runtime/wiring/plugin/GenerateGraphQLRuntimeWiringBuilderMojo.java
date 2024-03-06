package cool.klass.generator.graphql.runtime.wiring.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

import cool.klass.generator.graphql.runtime.wiring.GraphQLRuntimeWiringBuilderGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-graphql-runtime-wiring-builder",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateGraphQLRuntimeWiringBuilderMojo
        extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/graphql-runtime-wiring-builder")
    private File outputDirectory;

    @Parameter(property = "rootPackageName", required = true, readonly = true)
    private String rootPackageName;

    @Parameter(property = "applicationName", required = true, readonly = true)
    private String applicationName;

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
            GraphQLRuntimeWiringBuilderGenerator runtimeWiringGenerator = new GraphQLRuntimeWiringBuilderGenerator(
                    domainModel,
                    this.rootPackageName,
                    this.applicationName);
            runtimeWiringGenerator.writeRuntimeWiringBuilderFile(outputPath);
        }
        catch (RuntimeException | FileNotFoundException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
