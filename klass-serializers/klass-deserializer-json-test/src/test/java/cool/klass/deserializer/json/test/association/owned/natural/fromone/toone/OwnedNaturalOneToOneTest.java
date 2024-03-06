package cool.klass.deserializer.json.test.association.owned.natural.fromone.toone;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class OwnedNaturalOneToOneTest extends AbstractValidatorTest
{
    @Test
    public void validate_good() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "value": "value",
                  "target": {
                    "key": "key",
                    "value": "value",
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_backwards_association_end() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "value": "value",
                  "target": {
                    "key": "key",
                    "value": "value",
                    "source": {
                      "key": "key",
                      "value": "value",
                    },
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for opposite association end 'OwnedNaturalOneToOneTarget.source: OwnedNaturalOneToOneSource[1..1]' but value was object: {\"key\":\"key\",\"value\":\"value\"}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_extra_properties() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": "key",
                  "value": "value",
                  "target": {
                    "key": "key",
                    "sourceKey": "key",
                    "value": "value",
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource. Didn't expect to receive value for root key property 'OwnedNaturalOneToOneSource.key: String' but value was string: \"key\".",
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToOneTarget.sourceKey: String' but value was string: \"key\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "target": {}
                }""";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at OwnedNaturalOneToOneSource. Expected value for required property 'OwnedNaturalOneToOneSource.value: String' but value was missing.",
                "Error at OwnedNaturalOneToOneSource.target. Expected value for required property 'OwnedNaturalOneToOneTarget.key: String' but value was missing.",
                "Error at OwnedNaturalOneToOneSource.target. Expected value for required property 'OwnedNaturalOneToOneTarget.value: String' but value was missing.");

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_expected_actual_array() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": [],
                  "value": [],
                  "target": {
                    "key": [],
                    "sourceKey": [],
                    "value": [],
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at OwnedNaturalOneToOneSource.key. Expected property with type 'OwnedNaturalOneToOneSource.key: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToOneSource.value. Expected property with type 'OwnedNaturalOneToOneSource.value: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToOneSource.target.key. Expected property with type 'OwnedNaturalOneToOneTarget.key: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToOneSource.target.sourceKey. Expected property with type 'OwnedNaturalOneToOneTarget.sourceKey: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToOneSource.target.value. Expected property with type 'OwnedNaturalOneToOneTarget.value: String' but got '[]' with type 'array'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource. Didn't expect to receive value for root key property 'OwnedNaturalOneToOneSource.key: String' but value was array: [].",
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToOneTarget.sourceKey: String' but value was array: [].");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_object() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": {},
                  "value": {},
                  "target": {
                    "key": {},
                    "sourceKey": {},
                    "value": {},
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at OwnedNaturalOneToOneSource.key. Expected property with type 'OwnedNaturalOneToOneSource.key: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToOneSource.value. Expected property with type 'OwnedNaturalOneToOneSource.value: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToOneSource.target.key. Expected property with type 'OwnedNaturalOneToOneTarget.key: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToOneSource.target.sourceKey. Expected property with type 'OwnedNaturalOneToOneTarget.sourceKey: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToOneSource.target.value. Expected property with type 'OwnedNaturalOneToOneTarget.value: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource. Didn't expect to receive value for root key property 'OwnedNaturalOneToOneSource.key: String' but value was object: {}.",
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToOneTarget.sourceKey: String' but value was object: {}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_null() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": null,
                  "value": null,
                  "target": {
                    "key": null,
                    "sourceKey": null,
                    "value": null,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at OwnedNaturalOneToOneSource. Expected value for required property 'OwnedNaturalOneToOneSource.value: String' but value was null.",
                "Error at OwnedNaturalOneToOneSource.target. Expected value for required property 'OwnedNaturalOneToOneTarget.key: String' but value was null.",
                "Error at OwnedNaturalOneToOneSource.target. Expected value for required property 'OwnedNaturalOneToOneTarget.value: String' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource. Didn't expect to receive value for root key property 'OwnedNaturalOneToOneSource.key: String' but value was null.",
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToOneTarget.sourceKey: String' but value was null.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_boolean() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": true,
                  "value": true,
                  "target": {
                    "key": true,
                    "sourceKey": true,
                    "value": true,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at OwnedNaturalOneToOneSource.key. Expected property with type 'OwnedNaturalOneToOneSource.key: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToOneSource.value. Expected property with type 'OwnedNaturalOneToOneSource.value: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToOneSource.target.key. Expected property with type 'OwnedNaturalOneToOneTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToOneSource.target.sourceKey. Expected property with type 'OwnedNaturalOneToOneTarget.sourceKey: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToOneSource.target.value. Expected property with type 'OwnedNaturalOneToOneTarget.value: String' but got 'true' with type 'boolean'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource. Didn't expect to receive value for root key property 'OwnedNaturalOneToOneSource.key: String' but value was boolean: true.",
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToOneTarget.sourceKey: String' but value was boolean: true.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return this.domainModel.getClassByName("OwnedNaturalOneToOneSource");
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
