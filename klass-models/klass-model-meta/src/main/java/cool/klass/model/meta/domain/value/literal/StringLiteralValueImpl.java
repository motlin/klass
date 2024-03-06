package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.grammar.KlassParser.StringLiteralContext;

public final class StringLiteralValueImpl
        extends AbstractLiteralValue
        implements StringLiteralValue
{
    @Nonnull
    private final String value;

    private StringLiteralValueImpl(
            @Nonnull StringLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull String value)
    {
        super(elementContext, macroElement, sourceCode);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    @Override
    public StringLiteralContext getElementContext()
    {
        return (StringLiteralContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public String getValue()
    {
        return this.value;
    }

    public static final class StringLiteralValueBuilder
            extends AbstractLiteralValueBuilder<StringLiteralValueImpl>
    {
        @Nonnull
        private final String value;

        public StringLiteralValueBuilder(
                @Nonnull StringLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull String value)
        {
            super(elementContext, macroElement, sourceCode);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        @Nonnull
        protected StringLiteralValueImpl buildUnsafe()
        {
            return new StringLiteralValueImpl(
                    (StringLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.value);
        }
    }
}
