package cool.klass.model.converter.compiler;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.AbstractCompilerAnnotation;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class KlassCompilerTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void stackOverflow()
    {
        this.assertNoCompilerErrors("/com/stackoverflow/stackoverflow.klass");
    }

    @Test
    public void emoji()
    {
        this.assertNoCompilerErrors("emoji.klass");
    }

    @Test
    public void projectionOnInterface()
    {
        this.assertNoCompilerErrors("projectionOnInterface.klass");
    }

    private void assertNoCompilerErrors(@Nonnull String sourceCodeName)
    {
        String sourceCodeText = FileMatchRule.slurp(sourceCodeName, this.getClass());
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                sourceCodeName,
                sourceCodeText);
        KlassCompiler compiler = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();
        ImmutableList<RootCompilerAnnotation> compilerAnnotations = compilationResult
                .compilerAnnotations()
                .select(AbstractCompilerAnnotation::isError);

        assertThat(
                compilerAnnotations.makeString("\n"),
                compilerAnnotations,
                equalTo(Lists.immutable.empty()));

        assertTrue(compilationResult.domainModelWithSourceCode().isPresent());
    }
}
