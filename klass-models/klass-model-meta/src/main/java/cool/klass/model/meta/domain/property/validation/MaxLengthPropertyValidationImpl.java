package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.MaxLengthPropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationContext;

public class MaxLengthPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MaxLengthPropertyValidation
{
    public MaxLengthPropertyValidationImpl(
            @Nonnull MaxLengthValidationContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, sourceCode, owningProperty, number);
    }

    @Nonnull
    @Override
    public MaxLengthValidationContext getElementContext()
    {
        return (MaxLengthValidationContext) super.getElementContext();
    }

    public static class MaxLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxLengthPropertyValidationImpl>
    {
        public MaxLengthPropertyValidationBuilder(
                @Nonnull MaxLengthValidationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, sourceCode, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MaxLengthPropertyValidationImpl buildUnsafe()
        {
            return new MaxLengthPropertyValidationImpl(
                    (MaxLengthValidationContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
