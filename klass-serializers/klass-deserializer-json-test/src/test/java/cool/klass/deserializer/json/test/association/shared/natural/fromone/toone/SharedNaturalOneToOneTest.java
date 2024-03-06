package cool.klass.deserializer.json.test.association.shared.natural.fromone.toone;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import org.junit.Test;

public class SharedNaturalOneToOneTest extends AbstractValidatorTest
{
    @Test
    public void validate_good() throws IOException
    {
        this.validate("validate_good");
    }

    @Test
    public void validate_backwards_association_end() throws IOException
    {
        this.validate("validate_backwards_association_end");
    }

    // TODO: This should fail, or there should be an additional validation
    @Test
    public void validate_duplicate_keys() throws IOException
    {
        this.validate("validate_duplicate_keys");
    }

    @Test
    public void validate_extra_properties() throws IOException
    {
        this.validate("validate_extra_properties");
    }

    @Test
    public void validate_expected_actual_missing() throws IOException
    {
        this.validate("validate_expected_actual_missing");
    }

    @Test
    public void validate_expected_actual_array() throws IOException
    {
        this.validate("validate_expected_actual_array");
    }

    @Test
    public void validate_expected_actual_object() throws IOException
    {
        this.validate("validate_expected_actual_object");
    }

    @Test
    public void validate_expected_actual_null() throws IOException
    {
        this.validate("validate_expected_actual_null");
    }

    @Test
    public void validate_expected_actual_boolean() throws IOException
    {
        this.validate("validate_expected_actual_boolean");
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return this.domainModel.getClassByName("SharedNaturalOneToOneSource");
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
