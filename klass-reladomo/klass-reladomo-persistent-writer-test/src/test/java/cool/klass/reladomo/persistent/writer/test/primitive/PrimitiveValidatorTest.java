package cool.klass.reladomo.persistent.writer.test.primitive;

import java.io.IOException;

import org.junit.Test;

public interface PrimitiveValidatorTest
{
    @Test
    void validate_good() throws IOException;

    @Test
    void validate_mutation_context() throws IOException;

    @Test
    void validate_extra_properties() throws IOException;

    @Test
    void validate_expected_primitive_actual_missing() throws IOException;

    @Test
    void validate_expected_primitive_actual_array() throws IOException;

    @Test
    void validate_expected_primitive_actual_object() throws IOException;

    @Test
    void validate_expected_primitive_actual_null() throws IOException;

    @Test
    void validate_expected_primitive_actual_string() throws IOException;

    @Test
    void validate_version_expected_primitive_actual_object() throws IOException;
}
