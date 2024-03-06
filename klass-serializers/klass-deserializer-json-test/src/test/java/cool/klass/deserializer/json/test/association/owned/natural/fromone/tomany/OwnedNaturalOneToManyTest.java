package cool.klass.deserializer.json.test.association.owned.natural.fromone.tomany;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class OwnedNaturalOneToManyTest extends AbstractValidatorTest
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
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToManySource.target[0]. Didn't expect to receive value for opposite association end 'OwnedNaturalOneToManyTarget.source: OwnedNaturalOneToManySource[1..1]' but value was object: {\"key\":\"key\",\"value\":\"value\"}.",
                "Warning at OwnedNaturalOneToManySource.target[1]. Didn't expect to receive value for opposite association end 'OwnedNaturalOneToManyTarget.source: OwnedNaturalOneToManySource[1..1]' but value was object: {\"key\":\"key\",\"value\":\"value\"}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
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
                "Warning at OwnedNaturalOneToManySource. Didn't expect to receive value for root key property 'OwnedNaturalOneToManySource.key: String' but value was string: \"key\".",
                "Warning at OwnedNaturalOneToManySource.target[0]. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToManyTarget.sourceKey: String' but value was string: \"key\".",
                "Warning at OwnedNaturalOneToManySource.target[1]. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToManyTarget.sourceKey: String' but value was string: \"key\".");

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
                "Error at OwnedNaturalOneToManySource. Expected value for required property 'OwnedNaturalOneToManySource.value: String' but value was missing.",
                "Error at OwnedNaturalOneToManySource.target[0]. Expected value for required property 'OwnedNaturalOneToManyTarget.key: String' but value was missing.",
                "Error at OwnedNaturalOneToManySource.target[0]. Expected value for required property 'OwnedNaturalOneToManyTarget.value: String' but value was missing.");

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
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToManySource. Didn't expect to receive value for root key property 'OwnedNaturalOneToManySource.key: String' but value was array: [].",
                "Warning at OwnedNaturalOneToManySource.target[0]. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToManyTarget.sourceKey: String' but value was array: [].");

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
                "Error at OwnedNaturalOneToManySource.key. Expected property with type 'OwnedNaturalOneToManySource.key: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.value. Expected property with type 'OwnedNaturalOneToManySource.value: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.target[0].key. Expected property with type 'OwnedNaturalOneToManyTarget.key: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'OwnedNaturalOneToManyTarget.sourceKey: String' but got '{}' with type 'object'.",
                "Error at OwnedNaturalOneToManySource.target[0].value. Expected property with type 'OwnedNaturalOneToManyTarget.value: String' but got '{}' with type 'object'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToManySource. Didn't expect to receive value for root key property 'OwnedNaturalOneToManySource.key: String' but value was object: {}.",
                "Warning at OwnedNaturalOneToManySource.target[0]. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToManyTarget.sourceKey: String' but value was object: {}.");

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
                "Error at OwnedNaturalOneToManySource. Expected value for required property 'OwnedNaturalOneToManySource.value: String' but value was null.",
                "Error at OwnedNaturalOneToManySource.target[0]. Expected value for required property 'OwnedNaturalOneToManyTarget.key: String' but value was null.",
                "Error at OwnedNaturalOneToManySource.target[0]. Expected value for required property 'OwnedNaturalOneToManyTarget.value: String' but value was null.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToManySource. Didn't expect to receive value for root key property 'OwnedNaturalOneToManySource.key: String' but value was null.",
                "Warning at OwnedNaturalOneToManySource.target[0]. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToManyTarget.sourceKey: String' but value was null.");

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
                "Error at OwnedNaturalOneToManySource.key. Expected property with type 'OwnedNaturalOneToManySource.key: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.value. Expected property with type 'OwnedNaturalOneToManySource.value: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.target[0].key. Expected property with type 'OwnedNaturalOneToManyTarget.key: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.target[0].sourceKey. Expected property with type 'OwnedNaturalOneToManyTarget.sourceKey: String' but got 'true' with type 'boolean'.",
                "Error at OwnedNaturalOneToManySource.target[0].value. Expected property with type 'OwnedNaturalOneToManyTarget.value: String' but got 'true' with type 'boolean'.");
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToManySource. Didn't expect to receive value for root key property 'OwnedNaturalOneToManySource.key: String' but value was boolean: true.",
                "Warning at OwnedNaturalOneToManySource.target[0]. Didn't expect to receive value for foreign key property 'OwnedNaturalOneToManyTarget.sourceKey: String' but value was boolean: true.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Override
    protected Klass getKlass()
    {
        return CoverageExampleDomainModel.OwnedNaturalOneToManySource;
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
