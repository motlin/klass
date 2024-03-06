package cool.klass.deserializer.json.test.association.owned.natural.fromone.tomany;

import java.io.IOException;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class JsonTypeCheckingValidator_OwnedNaturalOneToManyTest extends AbstractValidatorTest
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

        this.validate(incomingJson, expectedErrors);
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

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

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
                "Error at OwnedNaturalOneToManySource.key. Expected property with type 'OwnedNaturalOneToManySource.key: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToManySource.value. Expected property with type 'OwnedNaturalOneToManySource.value: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToManySource.target[0].key. Expected property with type 'OwnedNaturalOneToManyTarget.key: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'OwnedNaturalOneToManyTarget.sourceKey: String' but got '[]' with type 'array'.",
                "Error at OwnedNaturalOneToManySource.target[0].value. Expected property with type 'OwnedNaturalOneToManyTarget.value: String' but got '[]' with type 'array'.");

        this.validate(incomingJson, expectedErrors);
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
                "Error at OwnedNaturalOneToManySource.key. Expected property with type 'OwnedNaturalOneToManySource.key: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.value. Expected property with type 'OwnedNaturalOneToManySource.value: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.target[0].key. Expected property with type 'OwnedNaturalOneToManyTarget.key: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'OwnedNaturalOneToManyTarget.sourceKey: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.target[0].value. Expected property with type 'OwnedNaturalOneToManyTarget.value: String' but got '{}' with type 'object'.");

        this.validate(incomingJson, expectedErrors);
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

        ImmutableList<String> expectedErrors = Lists.immutable.empty();

        this.validate(incomingJson, expectedErrors);
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
                "Error at OwnedNaturalOneToManySource.key. Expected property with type 'OwnedNaturalOneToManySource.key: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.value. Expected property with type 'OwnedNaturalOneToManySource.value: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.target[0].key. Expected property with type 'OwnedNaturalOneToManyTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'OwnedNaturalOneToManyTarget.sourceKey: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.target[0].value. Expected property with type 'OwnedNaturalOneToManyTarget.value: String' but got 'true' with type 'boolean'.");

        this.validate(incomingJson, expectedErrors);
    }

    @Override
    protected void performValidation(ObjectNode incomingInstance)
    {
        JsonTypeCheckingValidator.validate(
                incomingInstance,
                CoverageExampleDomainModel.OwnedNaturalOneToManySource,
                this.actualErrors);
    }
}
