package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.NumericPropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractNumericPropertyValidation
        extends AbstractPropertyValidation
        implements NumericPropertyValidation
{
    private final int number;

    protected AbstractNumericPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, sourceCode, owningProperty);
        this.number = number;
    }

    @Override
    public int getNumber()
    {
        return this.number;
    }

    public abstract static class NumericPropertyValidationBuilder<BuiltElement extends AbstractNumericPropertyValidation>
            extends PropertyValidationBuilder<BuiltElement>
    {
        protected final int number;

        protected NumericPropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> propertyBuilder,
                int number)
        {
            super(elementContext, macroElement, sourceCode, propertyBuilder);
            this.number = number;
        }
    }
}
