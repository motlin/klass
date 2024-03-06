package cool.klass.reladomo.persistent.writer.test.primitive.create;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.reladomo.persistent.writer.IncomingCreateDataModelValidator;
import cool.klass.reladomo.persistent.writer.test.AbstractValidatorTest;

public abstract class AbstractCreateValidatorTest
        extends AbstractValidatorTest
{
    @Override
    protected final void performValidation(@Nonnull ObjectNode incomingInstance, Object persistentInstance)
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

        IncomingCreateDataModelValidator.validate(
                this.reladomoDataStore,
                this.getKlass(),
                incomingInstance,
                this.actualErrors,
                this.actualWarnings);
    }
}
