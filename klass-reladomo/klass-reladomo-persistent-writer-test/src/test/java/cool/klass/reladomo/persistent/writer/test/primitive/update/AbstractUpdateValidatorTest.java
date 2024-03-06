package cool.klass.reladomo.persistent.writer.test.primitive.update;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.reladomo.persistent.writer.IncomingUpdateDataModelValidator;
import cool.klass.reladomo.persistent.writer.test.AbstractValidatorTest;

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

        IncomingUpdateDataModelValidator.validate(
                this.reladomoDataStore,
                this.getKlass(),
                persistentInstance,
                incomingInstance,
                this.actualErrors,
                this.actualWarnings);
    }
}
