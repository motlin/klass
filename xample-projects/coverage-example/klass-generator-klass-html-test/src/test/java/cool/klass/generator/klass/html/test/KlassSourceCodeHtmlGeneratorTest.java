package cool.klass.generator.klass.html.test;

import java.util.Optional;

import cool.klass.generator.klass.html.KlassSourceCodeHtmlGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class KlassSourceCodeHtmlGeneratorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        String              sourceCodeText = FileMatchRule.slurp("/com/stackoverflow/stackoverflow.klass", this.getClass());
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                "example.klass",
                sourceCodeText);
        KlassCompiler     compiler          = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();
        if (compilationResult.domainModelWithSourceCode().isEmpty())
        {
            String message = compilationResult.compilerAnnotations().makeString("\n");
            fail(message);
        }

        DomainModelWithSourceCode domainModel = compilationResult.domainModelWithSourceCode().get();
        assertThat(domainModel, notNullValue());

        for (SourceCode sourceCode : domainModel.getSourceCodes())
        {
            String fullPathSourceName = sourceCode.getFullPathSourceName();

            String html = KlassSourceCodeHtmlGenerator.getSourceCode(domainModel, sourceCode);

            String resourceClassPathLocation = fullPathSourceName + ".html";
            this.fileMatchRule.assertFileContents(
                    resourceClassPathLocation,
                    html);
        }
    }
}
