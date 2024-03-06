package cool.klass.reladomo.persistent.writer.test.primitive.create;

import java.time.Instant;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.reladomo.persistent.writer.IncomingCreateDataModelValidator;
import cool.klass.reladomo.persistent.writer.MutationContext;
import cool.klass.reladomo.persistent.writer.test.AbstractValidatorTest;
import io.liftwizard.reladomo.test.rule.ReladomoTestRuleBuilder;
import org.eclipse.collections.api.factory.Maps;
import org.junit.Rule;
import org.junit.rules.TestRule;

public abstract class AbstractCreateValidatorTest
        extends AbstractValidatorTest
{
    @Rule
    public final TestRule reladomoTestRule = new ReladomoTestRuleBuilder()
            .setRuntimeConfigurationPath("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml")
            .setTestDataFileNames("test-data/User.txt")
            .build();

    @Override
    protected final void validate(@Nonnull ObjectNode incomingInstance, Object persistentInstance)
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

        MutationContext mutationContext = new MutationContext(
                Optional.of("test user 1"),
                Instant.parse("1999-12-31T23:59:59.999Z"),
                Maps.immutable.empty());

        IncomingCreateDataModelValidator.validate(
                this.reladomoDataStore,
                this.domainModel.getUserClass().get(),
                this.getKlass(),
                mutationContext,
                incomingInstance,
                this.actualErrors,
                this.actualWarnings);
    }
}
