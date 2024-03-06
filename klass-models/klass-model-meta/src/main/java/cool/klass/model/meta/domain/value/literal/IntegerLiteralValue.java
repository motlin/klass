package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

public final class IntegerLiteralValue extends LiteralValue
{
    private final int value;

    private IntegerLiteralValue(@Nonnull ParserRuleContext elementContext, boolean inferred, int value)
    {
        super(elementContext, inferred);
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }

    @Override
    public void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitIntegerLiteral(this);
    }

    public static final class IntegerLiteralValueBuilder extends LiteralValueBuilder
    {
        private final int value;

        public IntegerLiteralValueBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred, int value)
        {
            super(elementContext, inferred);
            this.value = value;
        }

        @Nonnull
        @Override
        public IntegerLiteralValue build()
        {
            return new IntegerLiteralValue(this.elementContext, this.inferred, this.value);
        }
    }
}
