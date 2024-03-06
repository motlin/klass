package cool.klass.generator.klass.html.test;

import java.util.Optional;

import cool.klass.generator.klass.html.KlassTopLevelElementHtmlGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.test.constants.KlassTestConstants;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class KlassTopLevelElementHtmlGeneratorTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassTopLevelElementHtmlGeneratorTest.class);

    @Test
    public void smokeTest()
    {
        String sourceCodeText = KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT;
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                Optional.empty(),
                "example.klass",
                sourceCodeText);
        CompilerState     compilerState     = new CompilerState(compilationUnit);
        KlassCompiler     compiler          = new KlassCompiler(compilerState);
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
            DomainModel domainModel = domainModelCompilationResult.getDomainModel();
            assertThat(domainModel, notNullValue());

            for (PackageableElement topLevelElement : domainModel.getTopLevelElements())
            {
                String html = KlassTopLevelElementHtmlGenerator.writeHtml(topLevelElement);
                LOGGER.info(topLevelElement.getFullyQualifiedName());
                LOGGER.info(html);
            }
        }
        else
        {
            fail(compilationResult.getClass().getSimpleName());
        }
    }
}
