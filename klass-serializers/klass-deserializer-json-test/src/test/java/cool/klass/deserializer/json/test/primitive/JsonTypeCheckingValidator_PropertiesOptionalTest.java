package cool.klass.deserializer.json.test.primitive;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class JsonTypeCheckingValidator_PropertiesOptionalTest extends AbstractPrimitiveValidatorTest
{
    @Override
    @Test
    public void validate_good() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesOptionalId\": 1,\n"
                + "  \"optionalString\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalInteger\": 1,\n"
                + "  \"optionalLong\": 100000000000,\n"
                + "  \"optionalDouble\": 1.0123456789,\n"
                + "  \"optionalFloat\": 1.0123457,\n"
                + "  \"optionalBoolean\": true,\n"
                + "  \"optionalInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"optionalLocalDate\": \"1999-12-31\"\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    @Override
    public void validate_extra_properties() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesOptionalId\": 1,\n"
                + "  \"optionalString\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalInteger\": 1,\n"
                + "  \"optionalLong\": 100000000000,\n"
                + "  \"optionalDouble\": 1.0123456789,\n"
                + "  \"optionalFloat\": 1.0123457,\n"
                + "  \"optionalBoolean\": true,\n"
                + "  \"optionalInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"optionalLocalDate\": \"1999-12-31\",\n"
                + "  \"optionalDerived\": \"PropertiesOptional optionalDerived 1 ☝\",\n"
                + "  \"system\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": \"test user 1\",\n"
                + "  \"createdOn\": \"2000-01-01T04:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = "{}";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_array() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesOptionalId\": [],\n"
                + "  \"optionalString\": [],\n"
                + "  \"optionalInteger\": [],\n"
                + "  \"optionalLong\": [],\n"
                + "  \"optionalDouble\": [],\n"
                + "  \"optionalFloat\": [],\n"
                + "  \"optionalBoolean\": [],\n"
                + "  \"optionalInstant\": [],\n"
                + "  \"optionalLocalDate\": [],\n"
                + "  \"optionalDerived\": [],\n"
                + "  \"system\": [],\n"
                + "  \"systemFrom\": [],\n"
                + "  \"systemTo\": [],\n"
                + "  \"createdById\": [],\n"
                + "  \"createdOn\": [],\n"
                + "  \"lastUpdatedById\": []\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional.propertiesOptionalId. Expected property with type 'PropertiesOptional.propertiesOptionalId: Long' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalString. Expected property with type 'PropertiesOptional.optionalString: String?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalInteger. Expected property with type 'PropertiesOptional.optionalInteger: Integer?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalLong. Expected property with type 'PropertiesOptional.optionalLong: Long?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalDouble. Expected property with type 'PropertiesOptional.optionalDouble: Double?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalFloat. Expected property with type 'PropertiesOptional.optionalFloat: Float?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalBoolean. Expected property with type 'PropertiesOptional.optionalBoolean: Boolean?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalInstant. Expected property with type 'PropertiesOptional.optionalInstant: Instant?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalLocalDate. Expected property with type 'PropertiesOptional.optionalLocalDate: LocalDate?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.optionalDerived. Expected property with type 'PropertiesOptional.optionalDerived: String?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.system. Expected property with type 'PropertiesOptional.system: TemporalRange' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.systemFrom. Expected property with type 'PropertiesOptional.systemFrom: TemporalInstant' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.systemTo. Expected property with type 'PropertiesOptional.systemTo: TemporalInstant' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.createdById. Expected property with type 'PropertiesOptional.createdById: String' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.createdOn. Expected property with type 'PropertiesOptional.createdOn: Instant' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.lastUpdatedById. Expected property with type 'PropertiesOptional.lastUpdatedById: String' but got '[]' with type 'array'.");

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_object() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesOptionalId\": {},\n"
                + "  \"optionalString\": {},\n"
                + "  \"optionalInteger\": {},\n"
                + "  \"optionalLong\": {},\n"
                + "  \"optionalDouble\": {},\n"
                + "  \"optionalFloat\": {},\n"
                + "  \"optionalBoolean\": {},\n"
                + "  \"optionalInstant\": {},\n"
                + "  \"optionalLocalDate\": {},\n"
                + "  \"optionalDerived\": {},\n"
                + "  \"system\": {},\n"
                + "  \"systemFrom\": {},\n"
                + "  \"systemTo\": {},\n"
                + "  \"createdById\": {},\n"
                + "  \"createdOn\": {},\n"
                + "  \"lastUpdatedById\": {}\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional.propertiesOptionalId. Expected property with type 'PropertiesOptional.propertiesOptionalId: Long' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalString. Expected property with type 'PropertiesOptional.optionalString: String?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalInteger. Expected property with type 'PropertiesOptional.optionalInteger: Integer?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalLong. Expected property with type 'PropertiesOptional.optionalLong: Long?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalDouble. Expected property with type 'PropertiesOptional.optionalDouble: Double?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalFloat. Expected property with type 'PropertiesOptional.optionalFloat: Float?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalBoolean. Expected property with type 'PropertiesOptional.optionalBoolean: Boolean?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalInstant. Expected property with type 'PropertiesOptional.optionalInstant: Instant?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalLocalDate. Expected property with type 'PropertiesOptional.optionalLocalDate: LocalDate?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.optionalDerived. Expected property with type 'PropertiesOptional.optionalDerived: String?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.system. Expected property with type 'PropertiesOptional.system: TemporalRange' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.systemFrom. Expected property with type 'PropertiesOptional.systemFrom: TemporalInstant' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.systemTo. Expected property with type 'PropertiesOptional.systemTo: TemporalInstant' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.createdById. Expected property with type 'PropertiesOptional.createdById: String' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.createdOn. Expected property with type 'PropertiesOptional.createdOn: Instant' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.lastUpdatedById. Expected property with type 'PropertiesOptional.lastUpdatedById: String' but got '{}' with type 'object'.");

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_null() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesOptionalId\": null,\n"
                + "  \"optionalString\": null,\n"
                + "  \"optionalInteger\": null,\n"
                + "  \"optionalLong\": null,\n"
                + "  \"optionalDouble\": null,\n"
                + "  \"optionalFloat\": null,\n"
                + "  \"optionalBoolean\": null,\n"
                + "  \"optionalInstant\": null,\n"
                + "  \"optionalLocalDate\": null,\n"
                + "  \"optionalDerived\": null,\n"
                + "  \"system\": null,\n"
                + "  \"systemFrom\": null,\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": null,\n"
                + "  \"createdOn\": null,\n"
                + "  \"lastUpdatedById\": null\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_string() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesOptionalId\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalString\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalInteger\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalLong\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalDouble\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalFloat\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalBoolean\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalInstant\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalLocalDate\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalDerived\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"system\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"systemFrom\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"systemTo\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"createdById\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"createdOn\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"lastUpdatedById\": \"PropertiesOptional optionalString 1 ☝\"\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional.propertiesOptionalId. Expected property with type 'PropertiesOptional.propertiesOptionalId: Long' but got '\"PropertiesOptional optionalString 1 ☝\"' with type 'string'.",
                "Error at PropertiesOptional.optionalInteger. Expected property with type 'PropertiesOptional.optionalInteger: Integer?' but got '\"PropertiesOptional optionalString 1 ☝\"' with type 'string'.",
                "Error at PropertiesOptional.optionalLong. Expected property with type 'PropertiesOptional.optionalLong: Long?' but got '\"PropertiesOptional optionalString 1 ☝\"' with type 'string'.",
                "Error at PropertiesOptional.optionalDouble. Expected property with type 'PropertiesOptional.optionalDouble: Double?' but got '\"PropertiesOptional optionalString 1 ☝\"' with type 'string'.",
                "Error at PropertiesOptional.optionalFloat. Expected property with type 'PropertiesOptional.optionalFloat: Float?' but got '\"PropertiesOptional optionalString 1 ☝\"' with type 'string'.",
                "Error at PropertiesOptional.optionalBoolean. Expected property with type 'PropertiesOptional.optionalBoolean: Boolean?' but got '\"PropertiesOptional optionalString 1 ☝\"' with type 'string'.",
                "Incoming 'PropertiesOptional' has property 'optionalInstant: Instant?' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'optionalLocalDate: LocalDate?' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by LocalDate.parse().",
                "Incoming 'PropertiesOptional' has property 'system: TemporalRange system' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'systemFrom: TemporalInstant system from' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'systemTo: TemporalInstant system to' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'createdOn: Instant createdOn' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'");

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    protected void performValidation(ObjectNode incomingInstance)
    {
        JsonTypeCheckingValidator.validate(
                incomingInstance,
                CoverageExampleDomainModel.PropertiesOptional,
                this.actualErrors);
    }
}
