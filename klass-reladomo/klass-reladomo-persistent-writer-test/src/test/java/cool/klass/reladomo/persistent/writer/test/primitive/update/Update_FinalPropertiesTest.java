package cool.klass.reladomo.persistent.writer.test.primitive.update;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import io.liftwizard.reladomo.test.rule.ReladomoTestRuleBuilder;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class Update_FinalPropertiesTest
        extends AbstractUpdateValidatorTest
{
    @Rule
    public final TestRule reladomoTestRule = new ReladomoTestRuleBuilder()
            .setRuntimeConfigurationPath("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml")
            .setTestDataFileNames("test-data/Update_FinalPropertiesTest.txt")
            .enableDropCreateTables()
            .build();

    private Object persistentInstance;

    @Before
    public void setUp()
    {
        this.persistentInstance = this.reladomoDataStore.findByKey(
                CoverageExampleDomainModel.FinalProperties,
                Lists.immutable.with(1L));
    }

    @Test
    public void validate_mutate_final() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"id\": 1,\n"
                + "  \"data\": \"FinalProperties data 1 ☝\"\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, this.persistentInstance, expectedErrors);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return CoverageExampleDomainModel.FinalProperties;
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.REPLACE;
    }
}
