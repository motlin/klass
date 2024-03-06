package cool.klass.deserializer.json.test;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.model.meta.domain.api.Klass;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractValidatorTest
{
    protected final MutableList<String> actualErrors   = Lists.mutable.empty();
    protected final MutableList<String> actualWarnings = Lists.mutable.empty();

    private final ObjectMapper objectMapper = AbstractValidatorTest.getObjectMapper();

    protected final void validate(
            @Nonnull String incomingJson,
            @Nonnull ImmutableList<String> expectedErrors) throws IOException
    {
        this.validate(incomingJson, expectedErrors, Lists.immutable.empty());
    }

    protected final void validate(
            @Nonnull String incomingJson,
            @Nonnull ImmutableList<String> expectedErrors,
            @Nonnull ImmutableList<String> expectedWarnings) throws IOException
    {
        ObjectNode incomingInstance = (ObjectNode) this.objectMapper.readTree(incomingJson);
        this.performValidation(incomingInstance);
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

    protected final void performValidation(@Nonnull ObjectNode incomingInstance)
    {
        JsonTypeCheckingValidator.validate(
                incomingInstance,
                this.getKlass(),
                this.actualErrors);

        RequiredPropertiesValidator.validate(
                this.getKlass(),
                incomingInstance,
                this.getMode(),
                this.actualErrors,
                this.actualWarnings);
    }

    @Nonnull
    protected abstract Klass getKlass();

    @Nonnull
    protected abstract OperationMode getMode();
}
