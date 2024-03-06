package cool.klass.generator.graphql.schema.plugin;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;

import cool.klass.generator.grahql.schema.GraphQLSchemaGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-graphql-schema",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateGraphQLSchemaMojo extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-resources/graphql-schema")
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

        Path outputPath = this.outputDirectory.toPath();

        GraphQLSchemaGenerator generator = new GraphQLSchemaGenerator(
                domainModel,
                this.rootPackageName,
                this.applicationName,
                Instant.now());
        generator.writeSchemaFiles(outputPath);

        Resource resource = new Resource();
        resource.setDirectory(this.outputDirectory.getAbsolutePath());
        // TODO: Should be based on the output path
        this.mavenProject.addResource(resource);
    }
}
