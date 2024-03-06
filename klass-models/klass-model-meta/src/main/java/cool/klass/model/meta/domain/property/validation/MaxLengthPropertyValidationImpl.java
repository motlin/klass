package cool.klass.model.meta.domain.property.validation;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.validation.MaxLengthPropertyValidation;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class MaxLengthPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MaxLengthPropertyValidation
{
    public MaxLengthPropertyValidationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, inferred, owningProperty, number);
    }

    public static class MaxLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxLengthPropertyValidationImpl>
    {
        public MaxLengthPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, inferred, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MaxLengthPropertyValidationImpl buildUnsafe()
        {
            return new MaxLengthPropertyValidationImpl(
                    this.elementContext,
                    this.inferred,
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
