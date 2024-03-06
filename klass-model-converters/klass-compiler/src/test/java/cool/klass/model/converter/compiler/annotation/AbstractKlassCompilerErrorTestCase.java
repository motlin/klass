package cool.klass.model.converter.compiler.annotation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class AbstractKlassCompilerErrorTestCase
{
    @Rule
    public final FileMatchRule rule = new FileMatchRule();

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

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

        CompilationResult compilationResult = AbstractKlassCompilerErrorTestCase.getCompilationResult(
                sourceName,
                sourceCodeText);

        this.handleCompilerAnnotations(compilationResult, testName, callingClass);

        if (compilationResult.domainModelWithSourceCode().isPresent())
        {
            fail("Expected a compile error but found:\n" + sourceCodeText);
        }
    }

    protected void assertNoCompilerErrors()
    {
        Class<?> callingClass = this.getClass();
        String   testName     = callingClass.getSimpleName();
        String   sourceName   = testName + ".klass";

        String sourceCodeText = FileMatchRule.slurp(sourceName, callingClass);

        CompilationResult compilationResult = AbstractKlassCompilerErrorTestCase.getCompilationResult(
                sourceName,
                sourceCodeText);

        this.handleCompilerAnnotations(compilationResult, testName, callingClass);

        Optional<DomainModelWithSourceCode> domainModelWithSourceCode = compilationResult.domainModelWithSourceCode();
        assertTrue(domainModelWithSourceCode.isPresent());
    }

    @Nonnull
    private static CompilationResult getCompilationResult(String sourceName, String sourceCodeText)
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                sourceName,
                sourceCodeText);
        var compiler = new KlassCompiler(compilationUnit);
        return compiler.compile();
    }

    private void handleCompilerAnnotations(
            CompilationResult compilationResult,
            String testName,
            Class<?> callingClass)
    {
        ImmutableList<RootCompilerAnnotation> compilerAnnotations = compilationResult.compilerAnnotations();
        for (RootCompilerAnnotation compilerAnnotation : compilerAnnotations)
        {
            String annotationSourceName = "%s-%s-%d-%s.log".formatted(
                    testName,
                    compilerAnnotation.getLines().toReversed().makeString("_"),
                    compilerAnnotation.getCharPositionInLine(),
                    compilerAnnotation.getAnnotationCode());

            this.rule.assertFileContents(
                    annotationSourceName,
                    compilerAnnotation.toString(),
                    callingClass);
        }

        ImmutableListMultimap<Object, RootCompilerAnnotation> annotationsByKey =
                compilerAnnotations.groupBy(this::getAnnotationKey);
        annotationsByKey.forEachKeyMultiValues((key, compilerAnnotations1) ->
        {
            if (compilerAnnotations1.size() > 1)
            {
                fail("Found multiple compiler annotations for key: " + key);
            }
        });
    }

    private ImmutableList<Object> getAnnotationKey(RootCompilerAnnotation rootCompilerAnnotation)
    {
        return Lists.immutable.with(
                rootCompilerAnnotation.getFilenameWithoutDirectory(),
                rootCompilerAnnotation.getLine(),
                rootCompilerAnnotation.getCharPositionInLine(),
                rootCompilerAnnotation.getAnnotationCode());
    }
}
