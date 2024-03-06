package cool.klass.deserializer.json.test.association.shared.natural.frommany.toone;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class SharedNaturalManyToOneTest extends AbstractValidatorTest
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
                    "sources": [
                      {
                        "key": "key",
                        "value": "value",
                      },
                    ],
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

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
                "Error at SharedNaturalManyToOneSource.target.sourceKey. No such property 'SharedNaturalManyToOneTarget.sourceKey' but got \"key\". Expected properties: key, value, sources.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for root key property 'SharedNaturalManyToOneSource.key: String' but value was string: \"key\".",
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for foreign key property 'SharedNaturalManyToOneSource.targetKey: String' but value was string: \"key\".");

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
                "Error at SharedNaturalManyToOneSource. Expected value for required property 'SharedNaturalManyToOneSource.value: String' but value was missing.");

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
                    "value": [],
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalManyToOneSource.key. Expected property with type 'SharedNaturalManyToOneSource.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalManyToOneSource.targetKey. Expected property with type 'SharedNaturalManyToOneSource.targetKey: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalManyToOneSource.value. Expected property with type 'SharedNaturalManyToOneSource.value: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalManyToOneSource.target.key. Expected property with type 'SharedNaturalManyToOneTarget.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalManyToOneSource.target.value. Expected property with type 'SharedNaturalManyToOneTarget.value: String' but got '[]' with type 'array'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for root key property 'SharedNaturalManyToOneSource.key: String' but value was array: [].",
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for foreign key property 'SharedNaturalManyToOneSource.targetKey: String' but value was array: [].");

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
                    "value": {},
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalManyToOneSource.key. Expected property with type 'SharedNaturalManyToOneSource.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalManyToOneSource.targetKey. Expected property with type 'SharedNaturalManyToOneSource.targetKey: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalManyToOneSource.value. Expected property with type 'SharedNaturalManyToOneSource.value: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalManyToOneSource.target.key. Expected property with type 'SharedNaturalManyToOneTarget.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalManyToOneSource.target.value. Expected property with type 'SharedNaturalManyToOneTarget.value: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for root key property 'SharedNaturalManyToOneSource.key: String' but value was object: {}.",
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for foreign key property 'SharedNaturalManyToOneSource.targetKey: String' but value was object: {}.");

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
                    "value": null,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalManyToOneSource. Expected value for required property 'SharedNaturalManyToOneSource.value: String' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for root key property 'SharedNaturalManyToOneSource.key: String' but value was null.",
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for foreign key property 'SharedNaturalManyToOneSource.targetKey: String' but value was null.");

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
                    "value": true,
                  },
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalManyToOneSource.key. Expected property with type 'SharedNaturalManyToOneSource.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalManyToOneSource.targetKey. Expected property with type 'SharedNaturalManyToOneSource.targetKey: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalManyToOneSource.value. Expected property with type 'SharedNaturalManyToOneSource.value: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalManyToOneSource.target.key. Expected property with type 'SharedNaturalManyToOneTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalManyToOneSource.target.value. Expected property with type 'SharedNaturalManyToOneTarget.value: String' but got 'true' with type 'boolean'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for root key property 'SharedNaturalManyToOneSource.key: String' but value was boolean: true.",
                "Warning at SharedNaturalManyToOneSource. Didn't expect to receive value for foreign key property 'SharedNaturalManyToOneSource.targetKey: String' but value was boolean: true.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return this.domainModel.getClassByName("SharedNaturalManyToOneSource");
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
