package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import org.antlr.v4.runtime.ParserRuleContext;

public final class StringLiteralValueImpl extends AbstractLiteralValue implements StringLiteralValue
{
    @Nonnull
    private final String value;

    private StringLiteralValueImpl(@Nonnull ParserRuleContext elementContext, Optional<Element> macroElement, @Nonnull String value)
    {
        super(elementContext, macroElement);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    @Nonnull
    public String getValue()
    {
        return this.value;
    }

    public static final class StringLiteralValueBuilder extends AbstractLiteralValueBuilder<StringLiteralValueImpl>
    {
        @Nonnull
        private final String value;

        public StringLiteralValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull String value)
        {
            super(elementContext, macroElement);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        @Nonnull
        protected StringLiteralValueImpl buildUnsafe()
        {
            return new StringLiteralValueImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.value);
        }
    }
}
