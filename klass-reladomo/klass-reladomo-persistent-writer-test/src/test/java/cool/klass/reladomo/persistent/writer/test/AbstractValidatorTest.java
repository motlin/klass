package cool.klass.reladomo.persistent.writer.test;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.reladomo.persistent.writer.IncomingCreateDataModelValidator;
import io.liftwizard.dropwizard.configuration.uuid.seed.SeedUUIDSupplier;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Rule;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractValidatorTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    protected final MutableList<String> actualErrors      = Lists.mutable.empty();
    protected final MutableList<String> actualWarnings    = Lists.mutable.empty();
    protected final ReladomoDataStore   reladomoDataStore = this.getReladomoDataStore();
    private final   ObjectMapper        objectMapper      = AbstractValidatorTest.getObjectMapper();

    protected final void validate(
            @Nonnull String incomingJson,
            Object persistentInstance,
            @Nonnull ImmutableList<String> expectedErrors) throws IOException
    {
        this.validate(incomingJson, persistentInstance, expectedErrors, Lists.immutable.empty());
    }

    protected final void validate(
            @Nonnull String incomingJson,
            Object persistentInstance,
            @Nonnull ImmutableList<String> expectedErrors,
            @Nonnull ImmutableList<String> expectedWarnings) throws IOException
    {
        ObjectNode incomingInstance = (ObjectNode) this.objectMapper.readTree(incomingJson);
        this.performValidation(incomingInstance, persistentInstance);
        this.assertErrors(expectedErrors, expectedWarnings);
    }

    protected final void assertErrors(
            @Nonnull ImmutableList<String> expectedErrors,
            @Nonnull ImmutableList<String> expectedWarnings)
    {
        assertThat(
                this.actualErrors
                        .asLazy()
                        .collect(StringEscapeUtils::escapeJava)
                        .collect(each -> '"' + each + '"')
                        .makeString("\n", ",\n", "\n"),
                this.actualErrors,
                is(expectedErrors));

        assertThat(
                this.actualWarnings
                        .asLazy()
                        .collect(StringEscapeUtils::escapeJava)
                        .collect(each -> '"' + each + '"')
                        .makeString("\n", ",\n", "\n"),
                this.actualWarnings,
                is(expectedWarnings));
    }

    @Nonnull
    private static ObjectMapper getObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(Feature.ALLOW_YAML_COMMENTS, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(Feature.ALLOW_TRAILING_COMMA, true);
        return objectMapper;
    }

    protected abstract void performValidation(@Nonnull ObjectNode incomingInstance, Object persistentInstance);

    @Nonnull
    protected abstract Klass getKlass();

    @Nonnull
    protected abstract OperationMode getMode();

    @Nonnull
    private ReladomoDataStore getReladomoDataStore()
    {
        String           seed         = IncomingCreateDataModelValidator.class.getSimpleName();
        SeedUUIDSupplier uuidSupplier = new SeedUUIDSupplier(seed);
        return new ReladomoDataStore(uuidSupplier, 1);
    }
}
