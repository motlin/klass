package cool.klass.deserializer.json.test.association.shared.natural.fromone.tomany;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class SharedNaturalOneToManyTest extends AbstractValidatorTest
{
    @Test
    public void validate_good() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "value": "value",
                  "targets": [
                    {
                      "key": "key 1",
                      "value": "value 1",
                    },
                    {
                      "key": "key 2",
                      "value": "value 2",
                    }
                  ],
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
                  "targets": [
                    {
                      "key": "key 1",
                      "value": "value 1",
                      "source": {
                        "key": "key",
                        "value": "value",
                      },
                    },
                    {
                      "key": "key 2",
                      "value": "value 2",
                      "source": {
                        "key": "key",
                        "value": "value",
                      },
                    },
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    // TODO: This should fail, or there should be an additional validation
    @Test
    public void validate_duplicate_keys() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "value": "value",
                  "targets": [
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
                  "value": "value",
                  "targets": [
                    {
                      "key": "key 1",
                      "sourceKey": "key",
                      "value": "value 1",
                    },
                    {
                      "key": "key 2",
                      "sourceKey": "key",
                      "value": "value 2",
                    }
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was string: \"key\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = """
                {
                  "targets": [
                    {}
                  ]
                }""";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource. Expected value for required property 'SharedNaturalOneToManySource.value: String' but value was missing.");

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
                  "targets": [
                    {
                      "key": [],
                      "sourceKey": [],
                      "value": [],
                    },
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource.key. Expected property with type 'SharedNaturalOneToManySource.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.value. Expected property with type 'SharedNaturalOneToManySource.value: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.targets[0].key. Expected property with type 'SharedNaturalOneToManyTarget.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.targets[0].sourceKey. Expected property with type 'SharedNaturalOneToManyTarget.sourceKey: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.targets[0].value. Expected property with type 'SharedNaturalOneToManyTarget.value: String' but got '[]' with type 'array'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was array: [].");

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
                  "targets": [
                    {
                      "key": {},
                      "sourceKey": {},
                      "value": {},
                    },
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource.key. Expected property with type 'SharedNaturalOneToManySource.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.value. Expected property with type 'SharedNaturalOneToManySource.value: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.targets[0].key. Expected property with type 'SharedNaturalOneToManyTarget.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.targets[0].sourceKey. Expected property with type 'SharedNaturalOneToManyTarget.sourceKey: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.targets[0].value. Expected property with type 'SharedNaturalOneToManyTarget.value: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was object: {}.");

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
                  "targets": [
                    {
                      "key": null,
                      "sourceKey": null,
                      "value": null,
                    },
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource. Expected value for required property 'SharedNaturalOneToManySource.value: String' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was null.");

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
                  "targets": [
                    {
                      "key": true,
                      "sourceKey": true,
                      "value": true,
                    },
                  ],
                }
                """;

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource.key. Expected property with type 'SharedNaturalOneToManySource.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.value. Expected property with type 'SharedNaturalOneToManySource.value: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.targets[0].key. Expected property with type 'SharedNaturalOneToManyTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.targets[0].sourceKey. Expected property with type 'SharedNaturalOneToManyTarget.sourceKey: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.targets[0].value. Expected property with type 'SharedNaturalOneToManyTarget.value: String' but got 'true' with type 'boolean'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was boolean: true.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return this.domainModel.getClassByName("SharedNaturalOneToManySource");
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
