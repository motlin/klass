package cool.klass.generator.graphql.runtime.wiring.query.plugin;

import java.io.File;
import java.nio.file.Path;

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
            GraphQLQueryRuntimeWiringGenerator generator = new GraphQLQueryRuntimeWiringGenerator(
                    domainModel,
                    this.rootPackageName,
                    this.applicationName);
            generator.writeQueryRuntimeWiringFile(outputPath);
        }
        catch (RuntimeException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getPath());
    }
}
