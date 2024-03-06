package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.MaxPropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationContext;

public class MaxPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MaxPropertyValidation
{
    public MaxPropertyValidationImpl(
            @Nonnull MaxValidationContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, sourceCode, owningProperty, number);
    }

    @Nonnull
    @Override
    public MaxValidationContext getElementContext()
    {
        return (MaxValidationContext) super.getElementContext();
    }

    public static class MaxPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxPropertyValidationImpl>
    {
        public MaxPropertyValidationBuilder(
                @Nonnull MaxValidationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, sourceCode, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MaxPropertyValidationImpl buildUnsafe()
        {
            return new MaxPropertyValidationImpl(
                    (MaxValidationContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
