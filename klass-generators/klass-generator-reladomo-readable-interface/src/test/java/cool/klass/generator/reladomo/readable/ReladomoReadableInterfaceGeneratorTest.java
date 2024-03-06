package cool.klass.generator.reladomo.readable;

import java.util.Optional;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class ReladomoReadableInterfaceGeneratorTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        //<editor-fold desc="sourceCode">
        //language=Klass
        String klassSourceCode = ""
                + "package cool.klass.test\n"
                + "\n"
                + "class ClassWithDerivedProperty\n"
                + "{\n"
                + "    key                : String key;\n"
                + "\n"
                + "    derivedRequiredString      : String    derived;\n"
                + "    derivedRequiredInteger     : Integer   derived;\n"
                + "    derivedRequiredLong        : Long      derived;\n"
                + "    derivedRequiredDouble      : Double    derived;\n"
                + "    derivedRequiredFloat       : Float     derived;\n"
                + "    derivedRequiredBoolean     : Boolean   derived;\n"
                + "    derivedRequiredInstant     : Instant   derived;\n"
                + "    derivedRequiredLocalDate   : LocalDate derived;\n"
                + "\n"
                + "    derivedOptionalString      : String    ? derived;\n"
                + "    derivedOptionalInteger     : Integer   ? derived;\n"
                + "    derivedOptionalLong        : Long      ? derived;\n"
                + "    derivedOptionalDouble      : Double    ? derived;\n"
                + "    derivedOptionalFloat       : Float     ? derived;\n"
                + "    derivedOptionalBoolean     : Boolean   ? derived;\n"
                + "    derivedOptionalInstant     : Instant   ? derived;\n"
                + "    derivedOptionalLocalDate   : LocalDate ? derived;\n"
                + "}\n";
        //</editor-fold>

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

            //<editor-fold desc="expected java code">
            //language=JAVA
            String expectedSourceCode = ""
                    + "package cool.klass.test.reladomo.readable;\n"
                    + "\n"
                    + "import javax.annotation.*;\n"
                    + "import java.sql.*;\n"
                    + "import java.time.*;\n"
                    + "\n"
                    + "/**\n"
                    + " * Auto-generated by {@link cool.klass.generator.reladomo.readable.ReladomoReadableInterfaceGenerator}\n"
                    + " */\n"
                    + "public interface ReladomoReadableClassWithDerivedProperty\n"
                    + "{\n"
                    + "    // key\n"
                    + "    @Nonnull\n"
                    + "    String getKey();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    String getDerivedRequiredString();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    int getDerivedRequiredInteger();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    long getDerivedRequiredLong();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    double getDerivedRequiredDouble();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    float getDerivedRequiredFloat();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    boolean isDerivedRequiredBoolean();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    Timestamp getDerivedRequiredInstant();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    @Nonnull\n"
                    + "    Date getDerivedRequiredLocalDate();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    String getDerivedOptionalString();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    int getDerivedOptionalInteger();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    long getDerivedOptionalLong();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    double getDerivedOptionalDouble();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    float getDerivedOptionalFloat();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    boolean isDerivedOptionalBoolean();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    Timestamp getDerivedOptionalInstant();\n"
                    + "\n"
                    + "    // derived\n"
                    + "    Date getDerivedOptionalLocalDate();\n"
                    + "}\n";
            //</editor-fold>

            assertThat(javaSourceCode, javaSourceCode, is(expectedSourceCode));
        }
    }
}
