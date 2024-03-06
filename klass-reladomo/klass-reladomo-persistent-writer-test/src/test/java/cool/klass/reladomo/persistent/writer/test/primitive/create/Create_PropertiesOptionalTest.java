package cool.klass.reladomo.persistent.writer.test.primitive.create;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.reladomo.persistent.writer.test.primitive.PrimitiveValidatorTest;
import org.junit.Test;

public class Create_PropertiesOptionalTest
        extends AbstractCreateValidatorTest
        implements PrimitiveValidatorTest
{
    @Override
    @Test
    public void validate_good()
            throws IOException
    {
        this.validate("validate_good");
    }

    @Test
    @Override
    public void validate_mutation_context()
            throws IOException
    {
        this.validate("validate_mutation_context");
    }

    @Test
    @Override
    public void validate_extra_properties()
            throws IOException
    {
        this.validate("validate_extra_properties");
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_missing()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_missing");
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_array()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_array");
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_object()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_object");
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_null()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_null");
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_string()
            throws IOException
    {
        this.validate("validate_expected_primitive_actual_string");
    }

    @Test
    @Override
    public void validate_version_expected_primitive_actual_object()
            throws IOException
    {
        this.validate("validate_version_expected_primitive_actual_object");
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
        return OperationMode.CREATE;
    }
}
