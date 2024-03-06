package cool.klass.reladomo.persistent.writer.test.primitive.update;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import io.liftwizard.reladomo.test.rule.ReladomoTestRuleBuilder;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.ImmutableMap;
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
            .setTestDataFileNames("test-data/User.txt", "test-data/Update_FinalPropertiesTest.txt")
            .build();

    private Object persistentInstance;

    @Before
    public void setUp()
    {
        Klass            klass       = this.getKlass();
        DataTypeProperty keyProperty = (DataTypeProperty) klass.getPropertyByName("id").get();

        ImmutableMap<DataTypeProperty, Object> keys = Maps.immutable.with(keyProperty, 1L);

        this.persistentInstance = this.reladomoDataStore.findByKey(klass, keys);
    }

    @Test
    public void validate_mutate_final()
            throws IOException
    {
        this.validate("validate_mutate_final", this.persistentInstance);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return this.domainModel.getClassByName("FinalProperties");
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.REPLACE;
    }

    @Override
    protected ImmutableMap<DataTypeProperty, Object> getPropertyDataFromUrl()
    {
        DataTypeProperty dataTypeProperty = this.getKlass().getDataTypePropertyByName("id");
        return Maps.immutable.with(dataTypeProperty, 1L);
    }
}
