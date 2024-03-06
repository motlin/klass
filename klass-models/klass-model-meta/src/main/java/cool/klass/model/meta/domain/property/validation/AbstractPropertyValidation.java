package cool.klass.model.meta.domain.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
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
            Optional<Element> macroElement,
            AbstractDataTypeProperty<?> owningProperty)
    {
        super(elementContext, macroElement);
        this.owningProperty = Objects.requireNonNull(owningProperty);
    }

    public abstract static class PropertyValidationBuilder<BuiltElement extends AbstractPropertyValidation>
            extends ElementBuilder<BuiltElement>
    {
        protected final DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder;

        protected PropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder)
        {
            super(elementContext, macroElement);
            this.owningPropertyBuilder = Objects.requireNonNull(owningPropertyBuilder);
        }
    }
}
