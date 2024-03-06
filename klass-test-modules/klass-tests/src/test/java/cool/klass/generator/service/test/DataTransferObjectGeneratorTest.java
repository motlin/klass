package cool.klass.generator.service.test;

import java.util.Optional;

import cool.klass.generator.dto.DataTransferObjectsGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.test.constants.KlassTestConstants;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class DataTransferObjectGeneratorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule();

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void stackOverflow()
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                "example.klass",
                KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT);
        KlassCompiler     compiler          = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();

        if (compilationResult.domainModelWithSourceCode().isEmpty())
        {
            ImmutableList<RootCompilerAnnotation> compilerAnnotations = compilationResult.compilerAnnotations();
            String                                message             = compilerAnnotations.makeString("\n");
            fail(message);
        }
        else
        {
            DomainModelWithSourceCode domainModel = compilationResult.domainModelWithSourceCode().get();
            assertThat(domainModel, notNullValue());

            DataTransferObjectsGenerator dataTransferObjectsGenerator = new DataTransferObjectsGenerator(domainModel);

            Klass  klass           = domainModel.getClassByName("Question");
            String klassSourceCode = dataTransferObjectsGenerator.getClassSourceCode(klass);

            this.fileMatchRule.assertFileContents(
                    this.getClass().getSimpleName() + ".java",
                    klassSourceCode,
                    this.getClass());
        }
    }
}
