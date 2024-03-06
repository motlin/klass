package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import org.antlr.v4.runtime.ParserRuleContext;

public final class NullLiteralImpl extends AbstractLiteralValue implements NullLiteral
{
    private NullLiteralImpl(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    public static final class NullLiteralBuilder extends AbstractLiteralValueBuilder<NullLiteralImpl>
    {
        public NullLiteralBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Override
        @Nonnull
        protected NullLiteralImpl buildUnsafe()
        {
            return new NullLiteralImpl(this.elementContext, this.inferred);
        }
    }
}
