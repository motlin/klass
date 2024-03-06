package cool.klass.reladomo.persistent.writer.test.primitive.update;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.reladomo.persistent.writer.test.primitive.PrimitiveValidatorTest;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import com.liftwizard.reladomo.test.rule.ReladomoTestRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class Update_PropertiesRequiredTest
        extends AbstractUpdateValidatorTest
        implements PrimitiveValidatorTest
{
    @Rule
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule()
            .setRuntimeConfigurationPath("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml")
            .setTestDataFileNames("test-data/Update_PropertiesRequiredTest.txt")
            .enableDropCreateTables();

    private Object persistentInstance;

    @Before
    public void setUp()
    {
        this.persistentInstance = this.reladomoDataStore.findByKey(
                CoverageExampleDomainModel.PropertiesRequired,
                Lists.immutable.with(1L));
    }

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
                + "  \"requiredLocalDate\": \"1999-12-31\",\n"
                + "  \"version\": {\n"
                + "    \"number\": 1,\n"
                + "  },\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, this.persistentInstance, expectedErrors);
    }

    @Override
    @Test
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
                + "  \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "  \"version\": {\n"
                + "    \"propertiesRequiredId\": 1,\n"
                + "    \"number\": 1,\n"
                + "    \"system\": \"1999-12-31T23:59:59.999Z\",\n"
                + "    \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "    \"systemTo\": null,\n"
                + "    \"createdById\": \"test user 1\",\n"
                + "    \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "    \"lastUpdatedById\": \"test user 1\",\n"
                + "  },\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired.system. Mismatched value for property 'PropertiesRequired.system: TemporalRange?'. Expected absent value or 'null' but value was '1999-12-31T23:59:59.999Z'.",
                "Error at PropertiesRequired.version.system. Mismatched value for property 'PropertiesRequiredVersion.system: TemporalRange?'. Expected absent value or 'null' but value was '1999-12-31T23:59:59.999Z'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was string: \"PropertiesRequired requiredDerived 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange?' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant?' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant?' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was string: \"test user 1\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was string: \"test user 1\".");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
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
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLocalDate: LocalDate' but value was missing.",
                "Error at PropertiesRequired. Expected value for version property 'PropertiesRequired.version: PropertiesRequiredVersion[1..1]' but value was missing.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors);
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
                + "  \"lastUpdatedById\": [],\n"
                + "  \"version\": [],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired.propertiesRequiredId. Expected property with type 'PropertiesRequired.propertiesRequiredId: Long' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredString. Expected property with type 'PropertiesRequired.requiredString: String' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredInteger. Expected property with type 'PropertiesRequired.requiredInteger: Integer' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredLong. Expected property with type 'PropertiesRequired.requiredLong: Long' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredDouble. Expected property with type 'PropertiesRequired.requiredDouble: Double' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredFloat. Expected property with type 'PropertiesRequired.requiredFloat: Float' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredBoolean. Expected property with type 'PropertiesRequired.requiredBoolean: Boolean' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredInstant. Expected property with type 'PropertiesRequired.requiredInstant: Instant' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredLocalDate. Expected property with type 'PropertiesRequired.requiredLocalDate: LocalDate' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.requiredDerived. Expected property with type 'PropertiesRequired.requiredDerived: String' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.system. Expected property with type 'PropertiesRequired.system: TemporalRange?' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.systemFrom. Expected property with type 'PropertiesRequired.systemFrom: TemporalInstant?' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.systemTo. Expected property with type 'PropertiesRequired.systemTo: TemporalInstant?' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.createdById. Expected property with type 'PropertiesRequired.createdById: String' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.createdOn. Expected property with type 'PropertiesRequired.createdOn: Instant' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.lastUpdatedById. Expected property with type 'PropertiesRequired.lastUpdatedById: String' but got '[]' with type 'array'.",
                "Error at PropertiesRequired.version. Expected json object but value was array: [].");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange?' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant?' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant?' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was array: [].",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was array: [].");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
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
                + "  \"lastUpdatedById\": {},\n"
                + "  \"version\": {},\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired.propertiesRequiredId. Expected property with type 'PropertiesRequired.propertiesRequiredId: Long' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredString. Expected property with type 'PropertiesRequired.requiredString: String' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredInteger. Expected property with type 'PropertiesRequired.requiredInteger: Integer' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredLong. Expected property with type 'PropertiesRequired.requiredLong: Long' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredDouble. Expected property with type 'PropertiesRequired.requiredDouble: Double' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredFloat. Expected property with type 'PropertiesRequired.requiredFloat: Float' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredBoolean. Expected property with type 'PropertiesRequired.requiredBoolean: Boolean' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredInstant. Expected property with type 'PropertiesRequired.requiredInstant: Instant' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredLocalDate. Expected property with type 'PropertiesRequired.requiredLocalDate: LocalDate' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.requiredDerived. Expected property with type 'PropertiesRequired.requiredDerived: String' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.system. Expected property with type 'PropertiesRequired.system: TemporalRange?' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.systemFrom. Expected property with type 'PropertiesRequired.systemFrom: TemporalInstant?' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.systemTo. Expected property with type 'PropertiesRequired.systemTo: TemporalInstant?' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.createdById. Expected property with type 'PropertiesRequired.createdById: String' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.createdOn. Expected property with type 'PropertiesRequired.createdOn: Instant' but got '{}' with type 'object'.",
                "Error at PropertiesRequired.lastUpdatedById. Expected property with type 'PropertiesRequired.lastUpdatedById: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange?' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant?' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant?' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was object: {}.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was object: {}.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
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
                + "  \"lastUpdatedById\": null,\n"
                + "  \"version\": null,\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredString: String' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredInteger: Integer' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLong: Long' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredDouble: Double' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredFloat: Float' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredBoolean: Boolean' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredInstant: Instant' but value was null.",
                "Error at PropertiesRequired. Expected value for required property 'PropertiesRequired.requiredLocalDate: LocalDate' but value was null.",
                "Error at PropertiesRequired. Expected value for version property 'PropertiesRequired.version: PropertiesRequiredVersion[1..1]' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange?' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant?' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant?' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was null.",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was null.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
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
                + "  \"lastUpdatedById\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"version\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesRequired.propertiesRequiredId. Expected property with type 'PropertiesRequired.propertiesRequiredId: Long' but got '\"PropertiesRequired requiredString 1 ☝\"' with type 'string'.",
                "Error at PropertiesRequired.requiredInteger. Expected property with type 'PropertiesRequired.requiredInteger: Integer' but got '\"PropertiesRequired requiredString 1 ☝\"' with type 'string'.",
                "Error at PropertiesRequired.requiredLong. Expected property with type 'PropertiesRequired.requiredLong: Long' but got '\"PropertiesRequired requiredString 1 ☝\"' with type 'string'.",
                "Error at PropertiesRequired.requiredDouble. Expected property with type 'PropertiesRequired.requiredDouble: Double' but got '\"PropertiesRequired requiredString 1 ☝\"' with type 'string'.",
                "Error at PropertiesRequired.requiredFloat. Expected property with type 'PropertiesRequired.requiredFloat: Float' but got '\"PropertiesRequired requiredString 1 ☝\"' with type 'string'.",
                "Error at PropertiesRequired.requiredBoolean. Expected property with type 'PropertiesRequired.requiredBoolean: Boolean' but got '\"PropertiesRequired requiredString 1 ☝\"' with type 'string'.",
                "Incoming 'PropertiesRequired' has property 'requiredInstant: Instant' but got '\"PropertiesRequired requiredString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesRequired' has property 'requiredLocalDate: LocalDate' but got '\"PropertiesRequired requiredString 1 ☝\"'. Could not be parsed by LocalDate.parse().",
                "Incoming 'PropertiesRequired' has property 'system: TemporalRange? system' but got '\"PropertiesRequired requiredString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesRequired' has property 'systemFrom: TemporalInstant? system from' but got '\"PropertiesRequired requiredString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesRequired' has property 'systemTo: TemporalInstant? system to' but got '\"PropertiesRequired requiredString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Incoming 'PropertiesRequired' has property 'createdOn: Instant createdOn' but got '\"PropertiesRequired requiredString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Error at PropertiesRequired.version. Expected json object but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Error at PropertiesRequired.createdById. Mismatched value for property 'PropertiesRequired.createdById: String'. Expected absent value or 'test user 1' but value was 'PropertiesRequired requiredString 1 ☝'.",
                "Error at PropertiesRequired.lastUpdatedById. Mismatched value for property 'PropertiesRequired.lastUpdatedById: String'. Expected absent value or 'test user 1' but value was 'PropertiesRequired requiredString 1 ☝'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesRequired. Didn't expect to receive value for derived property 'PropertiesRequired.requiredDerived: String' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.system: TemporalRange?' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemFrom: TemporalInstant?' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for temporal property 'PropertiesRequired.systemTo: TemporalInstant?' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdById: String' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.createdOn: Instant' but value was string: \"PropertiesRequired requiredString 1 ☝\".",
                "Warning at PropertiesRequired. Didn't expect to receive value for audit property 'PropertiesRequired.lastUpdatedById: String' but value was string: \"PropertiesRequired requiredString 1 ☝\".");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return CoverageExampleDomainModel.PropertiesRequired;
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.REPLACE;
    }
}
