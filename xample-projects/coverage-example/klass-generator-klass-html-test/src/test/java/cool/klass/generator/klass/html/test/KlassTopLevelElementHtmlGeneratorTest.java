package cool.klass.generator.klass.html.test;

import java.util.Optional;

import cool.klass.generator.klass.html.KlassTopLevelElementHtmlGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.test.constants.KlassTestConstants;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class KlassTopLevelElementHtmlGeneratorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule();

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        String sourceCodeText = KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT;
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
        else
        {
            DomainModelWithSourceCode domainModel = compilationResult.domainModelWithSourceCode().get();
            assertThat(domainModel, notNullValue());

            for (TopLevelElement topLevelElement : domainModel.getTopLevelElements())
            {
                TopLevelElementWithSourceCode topLevelElementWithSourceCode = (TopLevelElementWithSourceCode) topLevelElement;

                String html = KlassTopLevelElementHtmlGenerator.writeHtml(domainModel, topLevelElementWithSourceCode);

                String resourceClassPathLocation = "%s_%s.html".formatted(
                        topLevelElement.getClass().getSimpleName(),
                        topLevelElement.getName());
                this.fileMatchRule.assertFileContents(
                        resourceClassPathLocation,
                        html,
                        this.getClass());
            }
        }
    }
}
