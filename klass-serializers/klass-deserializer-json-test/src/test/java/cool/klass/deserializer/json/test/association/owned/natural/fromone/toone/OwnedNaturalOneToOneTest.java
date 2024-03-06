package cool.klass.deserializer.json.test.association.owned.natural.fromone.toone;

import java.io.IOException;

import javax.annotation.Nonnull;

import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.test.AbstractValidatorTest;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.xample.coverage.meta.constants.CoverageExampleDomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class OwnedNaturalOneToOneTest extends AbstractValidatorTest
{
    @Test
    public void validate_good() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"value\": \"value\",\n"
                + "  \"target\": {\n"
                + "    \"key\": \"key\",\n"
                + "    \"value\": \"value\",\n"
                + "  },\n"
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
                + "  \"target\": {\n"
                + "    \"key\": \"key\",\n"
                + "    \"value\": \"value\",\n"
                + "    \"source\": {\n"
                + "      \"key\": \"key\",\n"
                + "      \"value\": \"value\",\n"
                + "    },\n"
                + "  },\n"
                + "}\n";

        ImmutableList<String> expectedErrors = Lists.immutable.empty();
        ImmutableList<String> expectedWarnings = Lists.immutable.with(
                "Warning at OwnedNaturalOneToOneSource.target. Didn't expect to receive value for opposite association end 'OwnedNaturalOneToOneTarget.source: OwnedNaturalOneToOneSource[1..1]' but value was object: {\"key\":\"key\",\"value\":\"value\"}.");

        this.validate(incomingJson, expectedErrors, expectedWarnings);
    }

    @Test
    public void validate_extra_properties() throws IOException
    {
        //language=JSON5
        String incomingJson = ""
                + "{\n"
                + "  \"key\": \"key\",\n"
                + "  \"value\": \"value\",\n"
                + "  \"target\": {\n"
                + "    \"key\": \"key\",\n"
                + "    \"sourceKey\": \"key\",\n"
                + "    \"value\": \"value\",\n"
                + "  },\n"
                + "}\n";

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
        String incomingJson = ""
                + "{\n"
                + "  \"target\": {}\n"
                + "}";

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
        String incomingJson = ""
                + "{\n"
                + "  \"key\": [],\n"
                + "  \"value\": [],\n"
                + "  \"target\": {\n"
                + "    \"key\": [],\n"
                + "    \"sourceKey\": [],\n"
                + "    \"value\": [],\n"
                + "  },\n"
                + "}\n";

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
        String incomingJson = ""
                + "{\n"
                + "  \"key\": {},\n"
                + "  \"value\": {},\n"
                + "  \"target\": {\n"
                + "    \"key\": {},\n"
                + "    \"sourceKey\": {},\n"
                + "    \"value\": {},\n"
                + "  },\n"
                + "}\n";

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
        String incomingJson = ""
                + "{\n"
                + "  \"key\": null,\n"
                + "  \"value\": null,\n"
                + "  \"target\": {\n"
                + "    \"key\": null,\n"
                + "    \"sourceKey\": null,\n"
                + "    \"value\": null,\n"
                + "  },\n"
                + "}\n";

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
        String incomingJson = ""
                + "{\n"
                + "  \"key\": true,\n"
                + "  \"value\": true,\n"
                + "  \"target\": {\n"
                + "    \"key\": true,\n"
                + "    \"sourceKey\": true,\n"
                + "    \"value\": true,\n"
                + "  },\n"
                + "}\n";

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
        return CoverageExampleDomainModel.OwnedNaturalOneToOneSource;
    }

    @Nonnull
    @Override
    protected OperationMode getMode()
    {
        return OperationMode.CREATE;
    }
}
