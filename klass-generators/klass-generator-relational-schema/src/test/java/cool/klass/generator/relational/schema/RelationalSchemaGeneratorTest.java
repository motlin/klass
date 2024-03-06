package cool.klass.generator.relational.schema;

import java.util.Optional;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class RelationalSchemaGeneratorTest
{
    @Test
    public void smokeTest()
    {
        //<editor-fold desc="sourceCode">
        //language=Klass
        String klassSourceCode = """
                package cool.klass.test

                class ClassWithDerivedProperty
                {
                    key                : String key;

                    derivedRequiredString      : String    derived;
                    derivedRequiredInteger     : Integer   derived;
                    derivedRequiredLong        : Long      derived;
                    derivedRequiredDouble      : Double    derived;
                    derivedRequiredFloat       : Float     derived;
                    derivedRequiredBoolean     : Boolean   derived;
                    derivedRequiredInstant     : Instant   derived;
                    derivedRequiredLocalDate   : LocalDate derived;

                    derivedOptionalString      : String    ? derived;
                    derivedOptionalInteger     : Integer   ? derived;
                    derivedOptionalLong        : Long      ? derived;
                    derivedOptionalDouble      : Double    ? derived;
                    derivedOptionalFloat       : Float     ? derived;
                    derivedOptionalBoolean     : Boolean   ? derived;
                    derivedOptionalInstant     : Instant   ? derived;
                    derivedOptionalLocalDate   : LocalDate ? derived;
                }
                """;
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
            String message = compilationResult.compilerAnnotations().makeString("\n");
            fail(message);
        }
        else
        {
            DomainModelWithSourceCode domainModel = compilationResult.domainModelWithSourceCode().get();
            assertThat(domainModel, notNullValue());

            RelationalSchemaGenerator generator = new RelationalSchemaGenerator(domainModel);

            Klass  klass          = domainModel.getClassByName("ClassWithDerivedProperty");
            String javaSourceCode = generator.getSourceCode(klass);

            //<editor-fold desc="expected ddl code">
            String expectedSourceCode = "";
            //</editor-fold>

            assertThat(javaSourceCode, javaSourceCode, is(expectedSourceCode));
        }
    }
}
