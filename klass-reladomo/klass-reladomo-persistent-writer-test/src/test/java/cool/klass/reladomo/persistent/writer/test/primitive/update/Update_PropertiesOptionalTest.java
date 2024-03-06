package cool.klass.reladomo.persistent.writer.test.primitive.update;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.reladomo.persistent.writer.test.primitive.PrimitiveValidatorTest;
import io.liftwizard.reladomo.test.rule.ReladomoTestRuleBuilder;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class Update_PropertiesOptionalTest
        extends AbstractUpdateValidatorTest
        implements PrimitiveValidatorTest
{
    @Rule
    public final TestRule reladomoTestRule = new ReladomoTestRuleBuilder()
            .setRuntimeConfigurationPath("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml")
            .setTestDataFileNames("test-data/User.txt", "test-data/Update_PropertiesOptionalTest.txt")
            .build();

    private Object persistentInstance;

    @Before
    public void setUp()
    {
        Klass            klass       = this.getKlass();
        DataTypeProperty keyProperty = (DataTypeProperty) klass.getPropertyByName("propertiesOptionalId").get();

        ImmutableMap<DataTypeProperty, Object> keys = Maps.immutable.with(keyProperty, 1L);

        this.persistentInstance = this.reladomoDataStore.findByKey(klass, keys);
    }

    @Override
    @Test
    public void validate_good()
            throws IOException
    {
        this.validate("validate_good", this.persistentInstance);
    }

    @Test
    @Override
    public void validate_mutation_context()
            throws IOException
    {
        this.validate("validate_mutation_context", this.persistentInstance);
    }

    @Test
    public void validate_wrong_version()
            throws IOException
    {
        this.validate("validate_wrong_version", this.persistentInstance);
    }

    @Override
    @Test
    public void validate_extra_properties()
            throws IOException
    {
        this.validate("validate_extra_properties", this.persistentInstance);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_missing()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_missing", this.persistentInstance);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_array()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_array", this.persistentInstance);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_object()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_object", this.persistentInstance);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_null()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_null", this.persistentInstance);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_string()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_string", this.persistentInstance);
    }

    @Test
    @Override
    public void validate_version_expected_primitive_actual_object()
            throws IOException
    {
        this.validate("validate_version_expected_primitive_actual_object", this.persistentInstance);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return this.domainModel.getClassByName("PropertiesOptional");
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.REPLACE;
    }
}
