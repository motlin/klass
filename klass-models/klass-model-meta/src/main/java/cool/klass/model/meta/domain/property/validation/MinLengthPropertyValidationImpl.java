package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.MinLengthPropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationContext;

public class MinLengthPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MinLengthPropertyValidation
{
    public MinLengthPropertyValidationImpl(
            @Nonnull MinLengthValidationContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, sourceCode, owningProperty, number);
    }

    @Nonnull
    @Override
    public MinLengthValidationContext getElementContext()
    {
        return (MinLengthValidationContext) super.getElementContext();
    }

    public static class MinLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MinLengthPropertyValidationImpl>
    {
        public MinLengthPropertyValidationBuilder(
                @Nonnull MinLengthValidationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, sourceCode, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MinLengthPropertyValidationImpl buildUnsafe()
        {
            return new MinLengthPropertyValidationImpl(
                    (MinLengthValidationContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
