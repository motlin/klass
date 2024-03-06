package cool.klass.reladomo.persistent.writer.test.primitive.update;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.reladomo.persistent.writer.test.primitive.PrimitiveValidatorTest;
import io.liftwizard.reladomo.test.rule.ReladomoTestRuleBuilder;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;
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
            .setTestDataFileNames("test-data/Update_PropertiesOptionalTest.txt")
            .enableDropCreateTables()
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
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": 1,
                  "optionalString": "PropertiesOptional optionalString 2 ✌",
                  "optionalInteger": 2,
                  "optionalLong": 200000000000,
                  "optionalDouble": 2.0123456789,
                  "optionalFloat": 2.0123456,
                  "optionalBoolean": false,
                  "optionalInstant": "2000-01-01T00:00:00Z",
                  "optionalLocalDate": "2000-01-01",
                  "version": {
                    "number": 1,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, this.persistentInstance, expectedErrors);
    }

    @Test
    public void validate_wrong_version()
            throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": 1,
                  "optionalString": "PropertiesOptional optionalString 2 ✌",
                  "optionalInteger": 2,
                  "optionalLong": 200000000000,
                  "optionalDouble": 2.0123456789,
                  "optionalFloat": 2.0123456,
                  "optionalBoolean": false,
                  "optionalInstant": "2000-01-01T00:00:00Z",
                  "optionalLocalDate": "2000-01-01",
                  "version": {
                    "number": 2,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional.version.number. Mismatched value for version property 'PropertiesOptionalVersion.number: Integer'. Expected absent value or '1' but value was '2'.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors);
    }

    @Override
    @Test
    public void validate_extra_properties()
            throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": 1,
                  "optionalString": "PropertiesOptional optionalString 1 ☝",
                  "optionalInteger": 1,
                  "optionalLong": 100000000000,
                  "optionalDouble": 1.0123456789,
                  "optionalFloat": 1.0123457,
                  "optionalBoolean": true,
                  "optionalInstant": "1999-12-31T23:59:00Z",
                  "optionalLocalDate": "1999-12-31",
                  "optionalDerived": "PropertiesOptional optionalDerived 1 ☝",
                  "system": "1999-12-31T23:59:59.999Z",
                  "systemFrom": "1999-12-31T23:59:59.999Z",
                  "systemTo": null,
                  "createdById": "test user 1",
                  "createdOn": "1999-12-31T23:59:59.999Z",
                  "lastUpdatedById": "test user 1",
                  "version": {
                    "propertiesOptionalId": 1,
                    "number": 1,
                    "system": "1999-12-31T23:59:59.999Z",
                    "systemFrom": "1999-12-31T23:59:59.999Z",
                    "systemTo": null,
                    "createdById": "test user 1",
                    "createdOn": "1999-12-31T23:59:59.999Z",
                    "lastUpdatedById": "test user 1",
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional.system. Mismatched value for temporal property 'PropertiesOptional.system: TemporalRange?'. Expected absent value or 'null' but value was '1999-12-31T23:59:59.999Z'.",
                "Error at PropertiesOptional.version.system. Mismatched value for temporal property 'PropertiesOptionalVersion.system: TemporalRange?'. Expected absent value or 'null' but value was '1999-12-31T23:59:59.999Z'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was string: \"PropertiesOptional optionalDerived 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was string: \"test user 1\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was string: \"1999-12-31T23:59:59.999Z\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was string: \"test user 1\".");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_missing()
            throws IOException
    {
        //language=JSON5
        String incomingJson = "{}";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional. Expected value for version property 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was missing.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_array()
            throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": [],
                  "optionalString": [],
                  "optionalInteger": [],
                  "optionalLong": [],
                  "optionalDouble": [],
                  "optionalFloat": [],
                  "optionalBoolean": [],
                  "optionalInstant": [],
                  "optionalLocalDate": [],
                  "optionalDerived": [],
                  "system": [],
                  "systemFrom": [],
                  "systemTo": [],
                  "createdById": [],
                  "createdOn": [],
                  "lastUpdatedById": [],
                  "version": [],
                }
                """;

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
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was array: [].");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_object()
            throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": {},
                  "optionalString": {},
                  "optionalInteger": {},
                  "optionalLong": {},
                  "optionalDouble": {},
                  "optionalFloat": {},
                  "optionalBoolean": {},
                  "optionalInstant": {},
                  "optionalLocalDate": {},
                  "optionalDerived": {},
                  "system": {},
                  "systemFrom": {},
                  "systemTo": {},
                  "createdById": {},
                  "createdOn": {},
                  "lastUpdatedById": {},
                  "version": {},
                }
                """;

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
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was object: {}.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_null()
            throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": null,
                  "optionalString": null,
                  "optionalInteger": null,
                  "optionalLong": null,
                  "optionalDouble": null,
                  "optionalFloat": null,
                  "optionalBoolean": null,
                  "optionalInstant": null,
                  "optionalLocalDate": null,
                  "optionalDerived": null,
                  "system": null,
                  "systemFrom": null,
                  "systemTo": null,
                  "createdById": null,
                  "createdOn": null,
                  "lastUpdatedById": null,
                  "version": null,
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at PropertiesOptional. Expected value for version property 'PropertiesOptional.version: PropertiesOptionalVersion[1..1]' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was null.",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was null.");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
    }

    @Override
    @Test
    public void validate_expected_primitive_actual_string()
            throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "propertiesOptionalId": "PropertiesOptional optionalString 1 ☝",
                  "optionalString": "PropertiesOptional optionalString 1 ☝",
                  "optionalInteger": "PropertiesOptional optionalString 1 ☝",
                  "optionalLong": "PropertiesOptional optionalString 1 ☝",
                  "optionalDouble": "PropertiesOptional optionalString 1 ☝",
                  "optionalFloat": "PropertiesOptional optionalString 1 ☝",
                  "optionalBoolean": "PropertiesOptional optionalString 1 ☝",
                  "optionalInstant": "PropertiesOptional optionalString 1 ☝",
                  "optionalLocalDate": "PropertiesOptional optionalString 1 ☝",
                  "optionalDerived": "PropertiesOptional optionalString 1 ☝",
                  "system": "PropertiesOptional optionalString 1 ☝",
                  "systemFrom": "PropertiesOptional optionalString 1 ☝",
                  "systemTo": "PropertiesOptional optionalString 1 ☝",
                  "createdById": "PropertiesOptional optionalString 1 ☝",
                  "createdOn": "PropertiesOptional optionalString 1 ☝",
                  "lastUpdatedById": "PropertiesOptional optionalString 1 ☝",
                  "version": "PropertiesOptional requiredString 1 ☝",
                }
                """;

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
                "Incoming 'PropertiesOptional' has property 'createdOn: Instant createdOn final' but got '\"PropertiesOptional optionalString 1 ☝\"'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                "Error at PropertiesOptional.version. Expected json object but value was string: \"PropertiesOptional requiredString 1 ☝\".",
                "Error at PropertiesOptional.createdById. Mismatched value for audit property 'PropertiesOptional.createdById: String'. Expected absent value or 'test user 1' but value was 'PropertiesOptional optionalString 1 ☝'.",
                "Error at PropertiesOptional.lastUpdatedById. Mismatched value for audit property 'PropertiesOptional.lastUpdatedById: String'. Expected absent value or 'test user 1' but value was 'PropertiesOptional optionalString 1 ☝'.");

        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at PropertiesOptional. Didn't expect to receive value for derived property 'PropertiesOptional.optionalDerived: String?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.system: TemporalRange?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemFrom: TemporalInstant?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for temporal property 'PropertiesOptional.systemTo: TemporalInstant?' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdById: String' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.createdOn: Instant' but value was string: \"PropertiesOptional optionalString 1 ☝\".",
                "Warning at PropertiesOptional. Didn't expect to receive value for audit property 'PropertiesOptional.lastUpdatedById: String' but value was string: \"PropertiesOptional optionalString 1 ☝\".");

        this.validate(incomingJson, this.persistentInstance, expectedErrors, expectedWarnings);
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
