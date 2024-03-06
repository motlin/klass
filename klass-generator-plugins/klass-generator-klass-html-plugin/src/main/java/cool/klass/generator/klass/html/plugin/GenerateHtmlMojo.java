package cool.klass.generator.klass.html.plugin;

import java.io.File;

import cool.klass.generator.klass.html.KlassSourceCodeHtmlGenerator;
import cool.klass.generator.plugin.AbstractGenerateMojo;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.collections.api.list.ImmutableList;

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
    public void execute() throws MojoExecutionException
    {
        CompilationResult compilationResult = this.getCompilationResult();

        this.handleErrorsCompilationResult(compilationResult);

        if (!(compilationResult instanceof DomainModelCompilationResult))
        {
            throw new AssertionError(compilationResult.getClass().getSimpleName());
        }

        ImmutableList<SourceCode>    sourceCodes = compilationResult.getSourceCodes();
        KlassSourceCodeHtmlGenerator generator   = new KlassSourceCodeHtmlGenerator(sourceCodes);
        generator.writeHtmlFiles(this.outputDirectory.toPath());
    }
}
