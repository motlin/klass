package cool.klass.model.meta.domain.property.validation;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.validation.MinLengthPropertyValidation;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class MinLengthPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MinLengthPropertyValidation
{
    public MinLengthPropertyValidationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, inferred, owningProperty, number);
    }

    public static class MinLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MinLengthPropertyValidationImpl>
    {
        public MinLengthPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, inferred, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MinLengthPropertyValidationImpl buildUnsafe()
        {
            return new MinLengthPropertyValidationImpl(
                    this.elementContext,
                    this.inferred,
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
