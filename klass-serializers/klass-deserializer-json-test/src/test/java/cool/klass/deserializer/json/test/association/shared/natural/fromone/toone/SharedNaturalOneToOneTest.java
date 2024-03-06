package cool.klass.deserializer.json.test.association.shared.natural.fromone.toone;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class SharedNaturalOneToOneTest extends AbstractValidatorTest
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
                    "source": [
                      {
                        "key": "key",
                        "value": "value",
                      },
                    ],
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.target.source. Expected json object but value was array: [{\"key\":\"key\",\"value\":\"value\"}].");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for outside projection property 'SharedNaturalOneToOneTarget.value: String' but value was string: \"value\".",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for opposite association end 'SharedNaturalOneToOneTarget.source: SharedNaturalOneToOneSource[1..1]' but value was array: [{\"key\":\"key\",\"value\":\"value\"}].");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    // TODO: This should fail, or there should be an additional validation
    @Test
    public void validate_duplicate_keys() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "value": "value",
                  "target": [
                    {
                      "key": "key 1",
                      "value": "value 1",
                    },
                    {
                      "key": "key 1",
                      "value": "value 1",
                    }
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.target. Expected json object but value was array: [{\"key\":\"key 1\",\"value\":\"value 1\"},{\"key\":\"key 1\",\"value\":\"value 1\"}].");

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_extra_properties() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": "key",
                  "targetKey": "key",
                  "value": "value",
                  "target": {
                    "key": "key",
                    "sourceKey": "key",
                    "value": "value",
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.targetKey. No such property 'SharedNaturalOneToOneSource.targetKey' but got \"key\". Expected properties: key, value, target.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToOneSource. Didn't expect to receive value for root key property 'SharedNaturalOneToOneSource.key: String' but value was string: \"key\".",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'SharedNaturalOneToOneTarget.sourceKey: String' but value was string: \"key\".",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for outside projection property 'SharedNaturalOneToOneTarget.value: String' but value was string: \"value\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "target": [
                    {}
                  ]
                }""";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.target. Expected json object but value was array: [{}].",
                "Error at SharedNaturalOneToOneSource. Expected value for required property 'SharedNaturalOneToOneSource.value: String' but value was missing.");

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_expected_actual_array() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": [],
                  "targetKey": [],
                  "value": [],
                  "target": {
                    "key": [],
                    "sourceKey": [],
                    "value": [],
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.key. Expected property with type 'SharedNaturalOneToOneSource.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToOneSource.targetKey. No such property 'SharedNaturalOneToOneSource.targetKey' but got []. Expected properties: key, value, target.",
                "Error at SharedNaturalOneToOneSource.value. Expected property with type 'SharedNaturalOneToOneSource.value: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToOneSource.target.key. Expected property with type 'SharedNaturalOneToOneTarget.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToOneSource.target.sourceKey. Expected property with type 'SharedNaturalOneToOneTarget.sourceKey: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToOneSource.target.value. Expected property with type 'SharedNaturalOneToOneTarget.value: String' but got '[]' with type 'array'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToOneSource. Didn't expect to receive value for root key property 'SharedNaturalOneToOneSource.key: String' but value was array: [].",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'SharedNaturalOneToOneTarget.sourceKey: String' but value was array: [].",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for outside projection property 'SharedNaturalOneToOneTarget.value: String' but value was array: [].");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_object() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": {},
                  "targetKey": {},
                  "value": {},
                  "target": {
                    "key": {},
                    "sourceKey": {},
                    "value": {},
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.key. Expected property with type 'SharedNaturalOneToOneSource.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToOneSource.targetKey. No such property 'SharedNaturalOneToOneSource.targetKey' but got {}. Expected properties: key, value, target.",
                "Error at SharedNaturalOneToOneSource.value. Expected property with type 'SharedNaturalOneToOneSource.value: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToOneSource.target.key. Expected property with type 'SharedNaturalOneToOneTarget.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToOneSource.target.sourceKey. Expected property with type 'SharedNaturalOneToOneTarget.sourceKey: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToOneSource.target.value. Expected property with type 'SharedNaturalOneToOneTarget.value: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToOneSource. Didn't expect to receive value for root key property 'SharedNaturalOneToOneSource.key: String' but value was object: {}.",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'SharedNaturalOneToOneTarget.sourceKey: String' but value was object: {}.",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for outside projection property 'SharedNaturalOneToOneTarget.value: String' but value was object: {}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_null() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": null,
                  "targetKey": null,
                  "value": null,
                  "target": {
                    "key": null,
                    "sourceKey": null,
                    "value": null,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.targetKey. No such property 'SharedNaturalOneToOneSource.targetKey' but got null. Expected properties: key, value, target.",
                "Error at SharedNaturalOneToOneSource. Expected value for required property 'SharedNaturalOneToOneSource.value: String' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToOneSource. Didn't expect to receive value for root key property 'SharedNaturalOneToOneSource.key: String' but value was null.",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'SharedNaturalOneToOneTarget.sourceKey: String' but value was null.",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for outside projection property 'SharedNaturalOneToOneTarget.value: String' but value was null.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_boolean() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "key": true,
                  "targetKey": true,
                  "value": true,
                  "target": {
                    "key": true,
                    "sourceKey": true,
                    "value": true,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToOneSource.key. Expected property with type 'SharedNaturalOneToOneSource.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToOneSource.targetKey. No such property 'SharedNaturalOneToOneSource.targetKey' but got true. Expected properties: key, value, target.",
                "Error at SharedNaturalOneToOneSource.value. Expected property with type 'SharedNaturalOneToOneSource.value: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToOneSource.target.key. Expected property with type 'SharedNaturalOneToOneTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToOneSource.target.sourceKey. Expected property with type 'SharedNaturalOneToOneTarget.sourceKey: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToOneSource.target.value. Expected property with type 'SharedNaturalOneToOneTarget.value: String' but got 'true' with type 'boolean'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToOneSource. Didn't expect to receive value for root key property 'SharedNaturalOneToOneSource.key: String' but value was boolean: true.",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for foreign key property 'SharedNaturalOneToOneTarget.sourceKey: String' but value was boolean: true.",
                "Warning at SharedNaturalOneToOneSource.target. Didn't expect to receive value for outside projection property 'SharedNaturalOneToOneTarget.value: String' but value was boolean: true.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
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
