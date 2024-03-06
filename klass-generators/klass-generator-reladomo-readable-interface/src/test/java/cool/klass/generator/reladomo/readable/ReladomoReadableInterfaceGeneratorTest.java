package cool.klass.generator.reladomo.readable;

import java.util.Optional;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class ReladomoReadableInterfaceGeneratorTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Test
    public void smokeTest()
    {
        String klassSourceCodeName = this.getClass().getSimpleName() + ".smokeTest.klass";

        String klassSourceCode     = FileMatchRule.slurp(klassSourceCodeName, this.getClass());

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                "example.klass",
                klassSourceCode);
        KlassCompiler     compiler          = new KlassCompiler(compilationUnit);
        CompilationResult compilationResult = compiler.compile();

        if (compilationResult.domainModelWithSourceCode().isEmpty())
        {
            ImmutableList<RootCompilerAnnotation> compilerAnnotations     = compilationResult.compilerAnnotations();
            String                                message                 = compilerAnnotations.makeString("\n");
            fail(message);
        }
        else
        {
            DomainModelWithSourceCode domainModel = compilationResult.domainModelWithSourceCode().get();
            assertThat(domainModel, notNullValue());

            ReladomoReadableInterfaceGenerator generator = new ReladomoReadableInterfaceGenerator(domainModel);

            Klass  klass          = domainModel.getClassByName("ClassWithDerivedProperty");
            String javaSourceCode = generator.getSourceCode(klass);

            String resourceClassPathLocation = this.getClass().getSimpleName() + ".smokeTest.java";
            this.fileMatchRule.assertFileContents(resourceClassPathLocation, javaSourceCode);
        }
    }
}
