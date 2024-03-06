package cool.klass.model.converter.compiler.annotation;

import java.util.Optional;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractKlassCompilerErrorTestCase
{
    @Rule
    public final FileMatchRule rule = new FileMatchRule();

    @Test
    public void smokeTest()
    {
        this.assertCompilerErrors();
    }

    protected void assertCompilerErrors()
    {
        Class<?> callingClass   = this.getClass();
        String   testName       = callingClass.getSimpleName();
        String   sourceName     = testName + ".klass";
        String   sourceCodeText = FileMatchRule.slurp(sourceName, callingClass);

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                sourceName,
                sourceCodeText);
        KlassCompiler     compiler          = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();
        // TODO: Split the ability to assert that there are errors and no domain model, from warnings and yes a domain model
        if (compilationResult.compilerAnnotations().isEmpty() || compilationResult.domainModelWithSourceCode().isPresent())
        {
            fail("Expected a compile error but found:\n" + sourceCodeText);
        }
        else
        {
            ImmutableList<RootCompilerAnnotation> compilerAnnotations     = compilationResult.compilerAnnotations();
            ImmutableList<String> compilerAnnotationStrings =
                    compilerAnnotations.collect(RootCompilerAnnotation::toString);

            for (int i = 0; i < compilerAnnotationStrings.size(); i++)
            {
                String errorSourceName          = String.format("%s-error-%d.log", testName, i);
                String compilerAnnotationString = compilerAnnotationStrings.get(i);
                this.rule.assertFileContents(errorSourceName, compilerAnnotationString, callingClass);
            }
        }
    }

    protected void assertNoCompilerErrors()
    {
        Class<?> callingClass = this.getClass();
        String   testName     = callingClass.getSimpleName();
        String   sourceName   = testName + ".klass";

        String sourceCodeText = FileMatchRule.slurp(sourceName, callingClass);

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                sourceName,
                sourceCodeText);
        KlassCompiler     compiler          = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();
        ImmutableList<RootCompilerAnnotation> compilerErrors = compilationResult
                .compilerAnnotations()
                .select(AbstractCompilerAnnotation::isError);
        if (compilerErrors.notEmpty())
        {
            String message = compilerErrors.makeString("\n");
            fail(message);
        }
        else
        {
            Optional<DomainModelWithSourceCode> domainModelWithSourceCode = compilationResult.domainModelWithSourceCode();
            assertTrue(domainModelWithSourceCode.isPresent());
        }
    }
}
