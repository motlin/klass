package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
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
            @Nonnull Optional<Element> macroElement,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, owningProperty, number);
    }

    public static class MinLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MinLengthPropertyValidationImpl>
    {
        public MinLengthPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MinLengthPropertyValidationImpl buildUnsafe()
        {
            return new MinLengthPropertyValidationImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
