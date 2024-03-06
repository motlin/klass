package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.MinPropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.MinValidationContext;

public class MinPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MinPropertyValidation
{
    public MinPropertyValidationImpl(
            @Nonnull MinValidationContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, sourceCode, owningProperty, number);
    }

    @Nonnull
    @Override
    public MinValidationContext getElementContext()
    {
        return (MinValidationContext) super.getElementContext();
    }

    public static class MinPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MinPropertyValidationImpl>
    {
        public MinPropertyValidationBuilder(
                @Nonnull MinValidationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder,
                int number)
        {
            super(elementContext, macroElement, sourceCode, owningPropertyBuilder, number);
        }

        @Nonnull
        @Override
        protected MinPropertyValidationImpl buildUnsafe()
        {
            return new MinPropertyValidationImpl(
                    (MinValidationContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
