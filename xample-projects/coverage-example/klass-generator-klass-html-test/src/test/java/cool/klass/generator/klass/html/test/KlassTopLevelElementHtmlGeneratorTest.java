package cool.klass.generator.klass.html.test;

import java.util.Optional;

import cool.klass.generator.klass.html.KlassTopLevelElementHtmlGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.test.constants.KlassTestConstants;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class KlassTopLevelElementHtmlGeneratorTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassTopLevelElementHtmlGeneratorTest.class);

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
        if (compilationResult instanceof ErrorsCompilationResult)
        {
            ErrorsCompilationResult          errorsCompilationResult = (ErrorsCompilationResult) compilationResult;
            ImmutableList<RootCompilerError> compilerErrors          = errorsCompilationResult.getCompilerErrors();
            String                           message                 = compilerErrors.makeString("\n");
            fail(message);
        }
        else if (compilationResult instanceof DomainModelCompilationResult)
        {
            DomainModelCompilationResult domainModelCompilationResult =
                    (DomainModelCompilationResult) compilationResult;
            DomainModelWithSourceCode domainModel = domainModelCompilationResult.getDomainModel();
            assertThat(domainModel, notNullValue());

            for (TopLevelElement topLevelElement : domainModel.getTopLevelElements())
            {
                TopLevelElementWithSourceCode topLevelElementWithSourceCode = (TopLevelElementWithSourceCode) topLevelElement;

                String html = KlassTopLevelElementHtmlGenerator.writeHtml(domainModel, topLevelElementWithSourceCode);
                LOGGER.info(html);
            }
        }
        else
        {
            fail(compilationResult.getClass().getSimpleName());
        }
    }
}
