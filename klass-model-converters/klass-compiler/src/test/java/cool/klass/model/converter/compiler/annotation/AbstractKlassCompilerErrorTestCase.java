package cool.klass.model.converter.compiler.annotation;

import java.util.Optional;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.DomainModel;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
        if (compilationResult instanceof DomainModelCompilationResult)
        {
            fail("Expected a compile error but found:\n" + sourceCodeText);
        }
        else if (compilationResult instanceof ErrorsCompilationResult)
        {
            ErrorsCompilationResult               errorsCompilationResult = (ErrorsCompilationResult) compilationResult;
            ImmutableList<RootCompilerAnnotation> compilerAnnotations     = errorsCompilationResult.getCompilerAnnotations();
            ImmutableList<String> compilerAnnotationStrings =
                    compilerAnnotations.collect(RootCompilerAnnotation::toString);

            for (int i = 0; i < compilerAnnotationStrings.size(); i++)
            {
                String errorSourceName          = String.format("%s-error-%d.log", testName, i);
                String compilerAnnotationString = compilerAnnotationStrings.get(i);
                this.rule.assertFileContents(errorSourceName, compilerAnnotationString, callingClass);
            }
        }
        else
        {
            fail(compilationResult.getClass().getSimpleName());
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
        if (compilationResult instanceof ErrorsCompilationResult)
        {
            ErrorsCompilationResult               errorsCompilationResult = (ErrorsCompilationResult) compilationResult;
            ImmutableList<RootCompilerAnnotation> compilerAnnotations     = errorsCompilationResult.getCompilerAnnotations();
            String                                message                 = compilerAnnotations.makeString("\n");
            fail(message);
        }
        else if (compilationResult instanceof DomainModelCompilationResult)
        {
            DomainModelCompilationResult domainModelCompilationResult =
                    (DomainModelCompilationResult) compilationResult;
            DomainModel domainModel = domainModelCompilationResult.getDomainModel();
            assertThat(domainModel, notNullValue());
        }
        else
        {
            fail(compilationResult.getClass().getSimpleName());
        }
    }
}