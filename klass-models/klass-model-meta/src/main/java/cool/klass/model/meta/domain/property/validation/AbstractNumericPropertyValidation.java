package cool.klass.model.meta.domain.property.validation;

import javax.annotation.Nonnull;

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
            boolean inferred,
            AbstractDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, inferred, owningProperty);
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
                boolean inferred,
                DataTypePropertyBuilder<?, ?, ?> propertyBuilder,
                int number)
        {
            super(elementContext, inferred, propertyBuilder);
            this.number = number;
        }
    }
}
