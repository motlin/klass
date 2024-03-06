package cool.klass.generator.graphql.schema.plugin;

import java.io.File;

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
public class GenerateGraphQLSchemaMojo
        extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-resources/graphql-schema")
    private File outputDirectory;

    @Override
    public void execute()
            throws MojoExecutionException
    {
        DomainModel domainModel = this.getDomainModel();

        var generator = new GraphQLSchemaGenerator(domainModel);
        generator.writeFiles(this.outputDirectory.toPath());

        Resource resource = new Resource();
        resource.setDirectory(this.outputDirectory.getAbsolutePath());
        this.mavenProject.addResource(resource);
    }
}
