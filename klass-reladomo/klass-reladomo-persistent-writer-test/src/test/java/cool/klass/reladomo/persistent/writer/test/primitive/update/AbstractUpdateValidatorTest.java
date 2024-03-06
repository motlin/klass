package cool.klass.reladomo.persistent.writer.test.primitive.update;

import java.time.Instant;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.reladomo.persistent.writer.IncomingUpdateDataModelValidator;
import cool.klass.reladomo.persistent.writer.MutationContext;
import cool.klass.reladomo.persistent.writer.test.AbstractValidatorTest;
import org.eclipse.collections.api.factory.Maps;

public abstract class AbstractUpdateValidatorTest
        extends AbstractValidatorTest
{
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

        IncomingUpdateDataModelValidator.validate(
                this.reladomoDataStore,
                this.domainModel.getUserClass().get(),
                this.getKlass(),
                mutationContext,
                persistentInstance,
                incomingInstance,
                this.actualErrors,
                this.actualWarnings);
    }
}
