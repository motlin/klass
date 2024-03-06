package cool.klass.generator.graphql.runtime.wiring.query.plugin;

import java.io.File;

import cool.klass.generator.graphql.runtime.wiring.query.GraphQLQueryRuntimeWiringGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-graphql-query-runtime-wiring",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateGraphQLQueryRuntimeWiringMojo
        extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/graphql-query-runtime-wiring")
    private File outputDirectory;

    @Override
    public void execute()
            throws MojoExecutionException
    {
        DomainModel domainModel = this.getDomainModel();
        var generator = new GraphQLQueryRuntimeWiringGenerator(domainModel);
        generator.writeFiles(this.outputDirectory.toPath());
        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
