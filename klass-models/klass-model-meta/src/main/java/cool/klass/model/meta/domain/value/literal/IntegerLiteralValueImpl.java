package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import org.antlr.v4.runtime.ParserRuleContext;

public final class IntegerLiteralValueImpl extends AbstractLiteralValue implements IntegerLiteralValue
{
    private final int value;

    private IntegerLiteralValueImpl(@Nonnull ParserRuleContext elementContext, boolean inferred, int value)
    {
        super(elementContext, inferred);
        this.value = value;
    }

    @Override
    public int getValue()
    {
        return this.value;
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
        public IntegerLiteralValueImpl build()
        {
            return new IntegerLiteralValueImpl(this.elementContext, this.inferred, this.value);
        }
    }
}
