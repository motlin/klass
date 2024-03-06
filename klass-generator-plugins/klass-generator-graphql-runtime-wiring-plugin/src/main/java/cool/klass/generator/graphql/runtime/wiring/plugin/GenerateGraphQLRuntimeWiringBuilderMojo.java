package cool.klass.generator.graphql.runtime.wiring.plugin;

import java.io.File;

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

    @Override
    public void execute() throws MojoExecutionException
    {
        DomainModel domainModel = this.getDomainModel();
        var generator = new GraphQLRuntimeWiringBuilderGenerator(domainModel);
        generator.writeFiles(this.outputDirectory.toPath());

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
