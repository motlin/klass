package cool.klass.deserializer.json.test.association.shared.natural.fromone.tomany;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class SharedNaturalOneToManyTest extends AbstractValidatorTest
{
    @Test
    public void validate_good() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"value\": \"value\",\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": \"key 1\",\n"
                + "      \"value\": \"value 1\",\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"key 2\",\n"
                + "      \"value\": \"value 2\",\n"
                + "    }\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_backwards_association_end() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"value\": \"value\",\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": \"key 1\",\n"
                + "      \"value\": \"value 1\",\n"
                + "      \"source\": {\n"
                + "        \"key\": \"key\",\n"
                + "        \"value\": \"value\",\n"
                + "      },\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"key 2\",\n"
                + "      \"value\": \"value 2\",\n"
                + "      \"source\": {\n"
                + "        \"key\": \"key\",\n"
                + "        \"value\": \"value\",\n"
                + "      },\n"
                + "    },\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    // TODO: This should fail, or there should be an additional validation
    @Test
    public void validate_duplicate_keys() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"value\": \"value\",\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": \"key 1\",\n"
                + "      \"value\": \"value 1\",\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"key 1\",\n"
                + "      \"value\": \"value 1\",\n"
                + "    }\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_extra_properties() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"key\": \"key\",\n"
                + "  \"value\": \"value\",\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": \"key 1\",\n"
                + "      \"sourceKey\": \"key\",\n"
                + "      \"value\": \"value 1\",\n"
                + "    },\n"
                + "    {\n"
                + "      \"key\": \"key 2\",\n"
                + "      \"sourceKey\": \"key\",\n"
                + "      \"value\": \"value 2\",\n"
                + "    }\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was string: \"key\".");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_missing() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"target\": [\n"
                + "    {}\n"
                + "  ]\n"
                + "}";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource. Expected value for required property 'SharedNaturalOneToManySource.value: String' but value was missing.");

        this.validate(incomingJson, expectedErrors);
    }

    @Test
    public void validate_expected_actual_array() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"key\": [],\n"
                + "  \"value\": [],\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": [],\n"
                + "      \"sourceKey\": [],\n"
                + "      \"value\": [],\n"
                + "    },\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource.key. Expected property with type 'SharedNaturalOneToManySource.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.value. Expected property with type 'SharedNaturalOneToManySource.value: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.target[0].key. Expected property with type 'SharedNaturalOneToManyTarget.key: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'SharedNaturalOneToManyTarget.sourceKey: String' but got '[]' with type 'array'.",
                "Error at SharedNaturalOneToManySource.target[0].value. Expected property with type 'SharedNaturalOneToManyTarget.value: String' but got '[]' with type 'array'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was array: [].");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_object() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"key\": {},\n"
                + "  \"value\": {},\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": {},\n"
                + "      \"sourceKey\": {},\n"
                + "      \"value\": {},\n"
                + "    },\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource.key. Expected property with type 'SharedNaturalOneToManySource.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.value. Expected property with type 'SharedNaturalOneToManySource.value: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.target[0].key. Expected property with type 'SharedNaturalOneToManyTarget.key: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'SharedNaturalOneToManyTarget.sourceKey: String' but got '{}' with type 'object'.",
                "Error at SharedNaturalOneToManySource.target[0].value. Expected property with type 'SharedNaturalOneToManyTarget.value: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was object: {}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_expected_actual_null() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"key\": null,\n"
                + "  \"value\": null,\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": null,\n"
                + "      \"sourceKey\": null,\n"
                + "      \"value\": null,\n"
                + "    },\n"
                + "  ],\n"
                + "}\n";

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
        String incomingJson = ""
                + "{\n"
                + "  \"key\": true,\n"
                + "  \"value\": true,\n"
                + "  \"target\": [\n"
                + "    {\n"
                + "      \"key\": true,\n"
                + "      \"sourceKey\": true,\n"
                + "      \"value\": true,\n"
                + "    },\n"
                + "  ],\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.with(
                "Error at SharedNaturalOneToManySource.key. Expected property with type 'SharedNaturalOneToManySource.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.value. Expected property with type 'SharedNaturalOneToManySource.value: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.target[0].key. Expected property with type 'SharedNaturalOneToManyTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'SharedNaturalOneToManyTarget.sourceKey: String' but got 'true' with type 'boolean'.",
                "Error at SharedNaturalOneToManySource.target[0].value. Expected property with type 'SharedNaturalOneToManyTarget.value: String' but got 'true' with type 'boolean'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at SharedNaturalOneToManySource. Didn't expect to receive value for root key property 'SharedNaturalOneToManySource.key: String' but value was boolean: true.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Nonnull
    @Override
    protected Klass getKlass()
    {
        return CoverageExampleDomainModel.SharedNaturalOneToManySource;
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
