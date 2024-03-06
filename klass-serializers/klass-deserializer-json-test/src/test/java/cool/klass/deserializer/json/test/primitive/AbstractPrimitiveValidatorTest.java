package cool.klass.deserializer.json.test.primitive;

import java.io.IOException;

import cool.klass.deserializer.json.test.AbstractValidatorTest;
import org.junit.Test;

public abstract class AbstractPrimitiveValidatorTest extends AbstractValidatorTest
{
    @Test
    public abstract void validate_good() throws IOException;

    @Test
    public abstract void validate_extra_properties() throws IOException;

    @Test
    public abstract void validate_expected_primitive_actual_missing() throws IOException;

    @Test
    public abstract void validate_expected_primitive_actual_array() throws IOException;

    @Test
    public abstract void validate_expected_primitive_actual_object() throws IOException;

    @Test
    public abstract void validate_expected_primitive_actual_null() throws IOException;

    @Test
    public abstract void validate_expected_primitive_actual_string() throws IOException;
}
