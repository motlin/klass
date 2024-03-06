package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
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
            @Nonnull Optional<Element> macroElement,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, owningProperty, number);
    }

    public static class MaxPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxPropertyValidationImpl>
    {
        public MaxPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MaxPropertyValidationImpl buildUnsafe()
        {
            return new MaxPropertyValidationImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
