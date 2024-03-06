package cool.klass.generator.relational.schema;

import java.util.Optional;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;
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
        String klassSourceCode = "package cool.klass.test\n"
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

        if (compilationResult instanceof ErrorsCompilationResult errorsCompilationResult)
        {
            ImmutableList<RootCompilerAnnotation> compilerAnnotations = errorsCompilationResult.compilerAnnotations();
            String                                message             = compilerAnnotations.makeString("\n");
            fail(message);
        }
        else if (compilationResult instanceof DomainModelCompilationResult domainModelCompilationResult)
        {
            DomainModel domainModel = domainModelCompilationResult.domainModel();
            assertThat(domainModel, notNullValue());

            RelationalSchemaGenerator generator = new RelationalSchemaGenerator(domainModel);

            Klass  klass          = domainModel.getClassByName("ClassWithDerivedProperty");
            String javaSourceCode = generator.getSourceCode(klass);

            //<editor-fold desc="expected ddl code">
            String expectedSourceCode = "";
            //</editor-fold>

            assertThat(javaSourceCode, javaSourceCode, is(expectedSourceCode));
        }
        else
        {
            fail(compilationResult.getClass().getSimpleName());
        }
    }
}
