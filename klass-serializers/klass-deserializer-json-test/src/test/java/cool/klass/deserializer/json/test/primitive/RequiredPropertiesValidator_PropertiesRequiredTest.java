package cool.klass.deserializer.json.test.primitive;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class RequiredPropertiesValidator_PropertiesRequiredTest extends AbstractPrimitiveValidatorTest
{
    @Override
    @Test
    public void validate_good() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesRequiredId\": 1,\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInteger\": 1,\n"
                + "  \"requiredLong\": 100000000000,\n"
                + "  \"requiredDouble\": 1.0123456789,\n"
                + "  \"requiredFloat\": 1.0123457,\n"
                + "  \"requiredBoolean\": true,\n"
                + "  \"requiredInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"requiredLocalDate\": \"1999-12-31\"\n"
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
                + "  \"propertiesRequiredId\": 1,\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInteger\": 1,\n"
                + "  \"requiredLong\": 100000000000,\n"
                + "  \"requiredDouble\": 1.0123456789,\n"
                + "  \"requiredFloat\": 1.0123457,\n"
                + "  \"requiredBoolean\": true,\n"
                + "  \"requiredInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"requiredLocalDate\": \"1999-12-31\",\n"
                + "  \"requiredDerived\": \"PropertiesRequired requiredDerived 1 ☝\",\n"
                + "  \"system\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": \"test user 1\",\n"
                + "  \"createdOn\": \"2000-01-01T04:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was string: \"PropertiesRequired requiredDerived 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was string: \"test user 1\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was string: \"2000-01-01T04:59:59.999Z\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was string: \"test user 1\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = "{}";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredString: String' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredInteger: Integer' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLong: Long' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredDouble: Double' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredFloat: Float' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredBoolean: Boolean' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredInstant: Instant' but value was missing.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLocalDate: LocalDate' but value was missing.");

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_array() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesRequiredId\": [],\n"
                + "  \"requiredString\": [],\n"
                + "  \"requiredInteger\": [],\n"
                + "  \"requiredLong\": [],\n"
                + "  \"requiredDouble\": [],\n"
                + "  \"requiredFloat\": [],\n"
                + "  \"requiredBoolean\": [],\n"
                + "  \"requiredInstant\": [],\n"
                + "  \"requiredLocalDate\": [],\n"
                + "  \"requiredDerived\": [],\n"
                + "  \"system\": [],\n"
                + "  \"systemFrom\": [],\n"
                + "  \"systemTo\": [],\n"
                + "  \"createdById\": [],\n"
                + "  \"createdOn\": [],\n"
                + "  \"lastUpdatedById\": []\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was array: [].");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_object() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesRequiredId\": {},\n"
                + "  \"requiredString\": {},\n"
                + "  \"requiredInteger\": {},\n"
                + "  \"requiredLong\": {},\n"
                + "  \"requiredDouble\": {},\n"
                + "  \"requiredFloat\": {},\n"
                + "  \"requiredBoolean\": {},\n"
                + "  \"requiredInstant\": {},\n"
                + "  \"requiredLocalDate\": {},\n"
                + "  \"requiredDerived\": {},\n"
                + "  \"system\": {},\n"
                + "  \"systemFrom\": {},\n"
                + "  \"systemTo\": {},\n"
                + "  \"createdById\": {},\n"
                + "  \"createdOn\": {},\n"
                + "  \"lastUpdatedById\": {}\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was object: {}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_null() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesRequiredId\": null,\n"
                + "  \"requiredString\": null,\n"
                + "  \"requiredInteger\": null,\n"
                + "  \"requiredLong\": null,\n"
                + "  \"requiredDouble\": null,\n"
                + "  \"requiredFloat\": null,\n"
                + "  \"requiredBoolean\": null,\n"
                + "  \"requiredInstant\": null,\n"
                + "  \"requiredLocalDate\": null,\n"
                + "  \"requiredDerived\": null,\n"
                + "  \"system\": null,\n"
                + "  \"systemFrom\": null,\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": null,\n"
                + "  \"createdOn\": null,\n"
                + "  \"lastUpdatedById\": null\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredString: String' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredInteger: Integer' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLong: Long' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredDouble: Double' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredFloat: Float' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredBoolean: Boolean' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredInstant: Instant' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLocalDate: LocalDate' but value was null.");

        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was null.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_string() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"propertiesRequiredId\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInteger\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredLong\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredDouble\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredFloat\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredBoolean\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInstant\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredLocalDate\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredDerived\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"system\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"systemFrom\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"systemTo\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"createdById\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"createdOn\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"lastUpdatedById\": \"PropertiesRequired requiredString 1 ☝\"\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was string: \"PropertiesRequired requiredString 1 ☝\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    protected void performValidation(ObjectNode incomingInstance)
    {
        RequiredPropertiesValidator.validate(
                CoverageExampleDomainModel.PropertiesRequired,
                incomingInstance,
                OperationMode.CREATE,
                this.actualErrors,
                this.actualWarnings);
    }
}
