package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.ExpressionValue;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class LiteralValue extends ExpressionValue
{
    protected LiteralValue(@Nonnull ParserRuleContext elementContext)
    {
        super(elementContext);
    }

    public abstract static class LiteralValueBuilder extends ExpressionValueBuilder
    {
        public LiteralValueBuilder(@Nonnull ParserRuleContext elementContext)
        {
            super(elementContext);
        }

        @Nonnull
        @Override
        public abstract LiteralValue build();
    }
}
