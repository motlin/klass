package cool.klass.model.meta.domain.property.validation;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.property.validation.PropertyValidation;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractPropertyValidation
        extends AbstractElement
        implements PropertyValidation
{
    private final AbstractDataTypeProperty<?> owningProperty;

    protected AbstractPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            AbstractDataTypeProperty<?> owningProperty)
    {
        super(elementContext, inferred);
        this.owningProperty = Objects.requireNonNull(owningProperty);
    }

    public abstract static class PropertyValidationBuilder<BuiltElement extends AbstractPropertyValidation>
            extends ElementBuilder<BuiltElement>
    {
        protected final DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder;

        protected PropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder)
        {
            super(elementContext, inferred);
            this.owningPropertyBuilder = Objects.requireNonNull(owningPropertyBuilder);
        }
    }
}
