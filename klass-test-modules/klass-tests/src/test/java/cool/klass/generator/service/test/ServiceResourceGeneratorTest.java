package cool.klass.generator.service.test;

import java.util.Optional;

import cool.klass.generator.service.ServiceResourceGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ServiceResourceGeneratorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void stackOverflow()
    {
        String              sourceCodeText = FileMatchRule.slurp("stackoverflow.klass", this.getClass());

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                0,
                Optional.empty(),
                "example.klass",
                sourceCodeText);
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

            ServiceResourceGenerator serviceResourceGenerator = new ServiceResourceGenerator(
                    domainModel,
                    "StackOverflow",
                    "com.stackoverflow");

            ServiceGroup serviceGroup           = domainModel.getServiceGroups().getOnly();
            String       serviceGroupSourceCode = serviceResourceGenerator.getServiceGroupSourceCode(serviceGroup);

            this.fileMatchRule.assertFileContents(
                    this.getClass().getSimpleName() + ".java",
                    serviceGroupSourceCode);
        }
    }
}
