package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

public final class StringLiteralValue extends LiteralValue
{
    @Nonnull
    private final String value;

    private StringLiteralValue(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String value)
    {
        super(elementContext, inferred);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public String getValue()
    {
        return this.value;
    }

    @Override
    public void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitStringLiteral(this);
    }

    public static final class StringLiteralValueBuilder extends LiteralValueBuilder
    {
        @Nonnull
        private final String value;

        public StringLiteralValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String value)
        {
            super(elementContext, inferred);
            this.value = Objects.requireNonNull(value);
        }

        @Nonnull
        @Override
        public StringLiteralValue build()
        {
            return new StringLiteralValue(this.elementContext, this.inferred, this.value);
        }
    }
}
