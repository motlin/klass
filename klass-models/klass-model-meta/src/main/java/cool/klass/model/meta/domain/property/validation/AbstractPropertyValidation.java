package cool.klass.model.meta.domain.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.validation.PropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
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
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull AbstractDataTypeProperty<?> owningProperty)
    {
        super(elementContext, macroElement, sourceCode);
        this.owningProperty = Objects.requireNonNull(owningProperty);
    }

    public abstract static class PropertyValidationBuilder<BuiltElement extends AbstractPropertyValidation>
            extends ElementBuilder<BuiltElement>
    {
        protected final DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder;

        protected PropertyValidationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder)
        {
            super(elementContext, macroElement, sourceCode);
            this.owningPropertyBuilder = Objects.requireNonNull(owningPropertyBuilder);
        }
    }
}
