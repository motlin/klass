package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
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
            Optional<Element> macroElement,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, owningProperty, number);
    }

    public static class MinPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MinPropertyValidationImpl>
    {
        public MinPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MinPropertyValidationImpl buildUnsafe()
        {
            return new MinPropertyValidationImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
