package cool.klass.deserializer.json.test.primitive;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class RequiredPropertiesValidator_PropertiesOptionalTest extends AbstractPrimitiveValidatorTest
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
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was string: \"PropertiesOptional optionalDerived 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was string: \"test user 1\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was string: \"2000-01-01T04:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was string: \"test user 1\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
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

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was array: [].",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was array: [].");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
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

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was object: {}.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was object: {}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
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
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was null.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
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

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was string: \"PropertiesOptional optionalString 1 ☝\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    protected void performValidation(ObjectNode incomingInstance)
    {
        RequiredPropertiesValidator.validate(
                CoverageExampleDomainModel.PropertiesOptional,
                incomingInstance,
                OperationMode.CREATE,
                this.actualErrors,
                this.actualWarnings);
    }
}
