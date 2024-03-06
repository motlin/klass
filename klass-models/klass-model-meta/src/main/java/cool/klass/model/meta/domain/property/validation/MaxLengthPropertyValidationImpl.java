package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
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
            Optional<Element> macroElement,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, owningProperty, number);
    }

    public static class MaxLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxLengthPropertyValidationImpl>
    {
        public MaxLengthPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MaxLengthPropertyValidationImpl buildUnsafe()
        {
            return new MaxLengthPropertyValidationImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
