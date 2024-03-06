package cool.klass.generator.klass.html.plugin;

import java.io.File;

import cool.klass.generator.klass.html.KlassSourceCodeHtmlGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
        name = "generate-klass-html",
        defaultPhase = LifecyclePhase.GENERATE_SOURCES,
        threadSafe = true,
        requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateHtmlMojo
        extends AbstractGenerateMojo
{
    @Parameter(
            property = "outputDirectory",
            defaultValue = "${project.build.directory}/generated-sources/html")
    private File outputDirectory;

    @Override
    public void execute()
            throws MojoExecutionException
    {
        CompilationResult compilationResult = this.getCompilationResult();

        this.handleErrorsCompilationResult(compilationResult);

        DomainModelWithSourceCode domainModel = compilationResult.domainModelWithSourceCode().get();
        var                       generator   = new KlassSourceCodeHtmlGenerator(domainModel);
        generator.writeHtmlFiles(this.outputDirectory.toPath());
    }
}
