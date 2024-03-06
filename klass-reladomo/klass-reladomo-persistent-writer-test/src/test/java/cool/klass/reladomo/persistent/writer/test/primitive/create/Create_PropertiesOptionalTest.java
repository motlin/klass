package cool.klass.reladomo.persistent.writer.test.primitive.create;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.reladomo.persistent.writer.test.primitive.PrimitiveValidatorTest;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class Create_PropertiesOptionalTest
        extends AbstractCreateValidatorTest
        implements PrimitiveValidatorTest
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

        this.validate(incomingJson, null, expectedErrors);
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
                + "  \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "  \"version\": {\n"
                + "    \"propertiesOptionalId\": 1,\n"
                + "    \"number\": 1,\n"
                + "    \"system\": \"1999-12-31T23:59:59.999Z\",\n"
                + "    \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "    \"systemTo\": null,\n"
                + "    \"createdById\": \"test user 1\",\n"
                + "    \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "    \"lastUpdatedById\": \"test user 1\",\n"
                + "  },\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was string: \"PropertiesOptional optionalDerived 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was string: \"test user 1\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was string: \"test user 1\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for version association end 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was object: {\"propertiesOptionalId\":1,\"number\":1,\"system\":\"1999-12-31T23:59:59.999Z\",\"systemFrom\":\"1999-12-31T23:59:59.999Z\",\"systemTo\":null,\"createdById\":\"test user 1\",\"createdOn\":\"1999-12-31T23:59:59.999Z\",\"lastUpdatedById\":\"test user 1\"}.");

        this.validate(incomingJson, null, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = "{}";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, null, expectedErrors);
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
                + "  \"lastUpdatedById\": [],\n"
                + "  \"version\": [],\n"
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
                "Error at PropertiesOptional.system. Expected property with type 'PropertiesOptional.system: TemporalRange?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.systemFrom. Expected property with type 'PropertiesOptional.systemFrom: TemporalInstant?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.systemTo. Expected property with type 'PropertiesOptional.systemTo: TemporalInstant?' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.createdById. Expected property with type 'PropertiesOptional.createdById: String' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.createdOn. Expected property with type 'PropertiesOptional.createdOn: Instant' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.lastUpdatedById. Expected property with type 'PropertiesOptional.lastUpdatedById: String' but got '[]' with type 'array'.",
                "Error at PropertiesOptional.version. Expected json object but value was array: [].");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for version association end 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was array: [].");

        this.validate(incomingJson, null, expectedErrors, expectedWarnings);
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
                + "  \"lastUpdatedById\": {},\n"
                + "  \"version\": {},\n"
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
                "Error at PropertiesOptional.system. Expected property with type 'PropertiesOptional.system: TemporalRange?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.systemFrom. Expected property with type 'PropertiesOptional.systemFrom: TemporalInstant?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.systemTo. Expected property with type 'PropertiesOptional.systemTo: TemporalInstant?' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.createdById. Expected property with type 'PropertiesOptional.createdById: String' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.createdOn. Expected property with type 'PropertiesOptional.createdOn: Instant' but got '{}' with type 'object'.",
                "Error at PropertiesOptional.lastUpdatedById. Expected property with type 'PropertiesOptional.lastUpdatedById: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for version association end 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was object: {}.");

        this.validate(incomingJson, null, expectedErrors, expectedWarnings);
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
                + "  \"lastUpdatedById\": null,\n"
                + "  \"version\": null,\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for version association end 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was null.");

        this.validate(incomingJson, null, expectedErrors, expectedWarnings);
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
                + "  \"lastUpdatedById\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"version\": \"PropertiesOptional requiredString 1 ☝\",\n"
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
                "Incoming 'PropertiesOptional' has property 'system: TemporalRange? system' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'systemFrom: TemporalInstant? system from' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'systemTo: TemporalInstant? system to' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesOptional' has property 'createdOn: Instant createdOn' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Error at PropertiesOptional.version. Expected json object but value was string: \"PropertiesOptional requiredString 1 ☝\".");

        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for version association end 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was string: \"PropertiesOptional requiredString 1 ☝\".");

        this.validate(incomingJson, null, expectedErrors, expectedWarnings);
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
