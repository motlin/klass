package cool.klass.model.meta.domain.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.NumericPropertyValidation;
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
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, macroElement, owningProperty);
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
                DataTypePropertyBuilder<?, ?, ?> propertyBuilder,
                int number)
        {
            super(elementContext, macroElement, propertyBuilder);
            this.number = number;
        }
    }
}
