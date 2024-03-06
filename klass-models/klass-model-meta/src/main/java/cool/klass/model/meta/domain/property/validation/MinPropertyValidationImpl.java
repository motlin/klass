package cool.klass.model.meta.domain.property.validation;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.validation.MinPropertyValidation;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class MinPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MinPropertyValidation
{
    public MinPropertyValidationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, inferred, owningProperty, number);
    }

    public static class MinPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MinPropertyValidationImpl>
    {
        public MinPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, inferred, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MinPropertyValidationImpl buildUnsafe()
        {
            return new MinPropertyValidationImpl(
                    this.elementContext,
                    this.inferred,
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
