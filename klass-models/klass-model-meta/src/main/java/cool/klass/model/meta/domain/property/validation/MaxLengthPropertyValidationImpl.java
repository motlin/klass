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
import org.antlr.v4.runtime.ParserRuleContext;

public class MaxLengthPropertyValidationImpl
        extends AbstractNumericPropertyValidation
        implements MaxLengthPropertyValidation
{
    public MaxLengthPropertyValidationImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, sourceCode, owningProperty, number);
    }

    public static class MaxLengthPropertyValidationBuilder
            extends NumericPropertyValidationBuilder<MaxLengthPropertyValidationImpl>
    {
        public MaxLengthPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
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
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.owningPropertyBuilder.getElement(),
                    this.number);
        }
    }
}
