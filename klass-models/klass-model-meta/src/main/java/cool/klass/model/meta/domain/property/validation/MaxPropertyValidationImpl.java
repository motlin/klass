package cool.klass.model.meta.domain.property.validation;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.validation.MaxPropertyValidation;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class MaxPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MaxPropertyValidation
{
    public MaxPropertyValidationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, inferred, owningProperty, number);
    }

    public static class MaxPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxPropertyValidationImpl>
    {
        public MaxPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, inferred, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MaxPropertyValidationImpl buildUnsafe()
        {
            return new MaxPropertyValidationImpl(
                    this.elementContext,
                    this.inferred,
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
