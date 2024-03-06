package cool.klass.generator.reladomo.merge.hooks.plugin;

import java.io.File;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.generator.reladomo.mergehook.ReladomoMergeHookGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-reladomo-merge-hooks",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateReladomoMergeHooksMojo extends AbstractGenerateMojo
{
    @Parameter(property = "outputDirectory",
               defaultValue = "${project.build.directory}/generated-sources/reladomo-merge-hooks")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException
    {
        if (!this.outputDirectory.exists())
        {
            this.outputDirectory.mkdirs();
        }

        DomainModel domainModel = this.getDomainModel();

        ReladomoMergeHookGenerator generator = new ReladomoMergeHookGenerator(domainModel);

        generator.writeMergeHookFiles(this.outputDirectory.toPath());

        this.mavenProject.addCompileSourceRoot(this.outputDirectory.getAbsolutePath());
    }
}
