package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import org.antlr.v4.runtime.ParserRuleContext;

public final class UserLiteralImpl extends AbstractLiteralValue implements UserLiteral
{
    private UserLiteralImpl(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    public static final class UserLiteralBuilder extends LiteralValueBuilder
    {
        public UserLiteralBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Nonnull
        @Override
        public UserLiteralImpl build()
        {
            return new UserLiteralImpl(this.elementContext, this.inferred);
        }
    }
}
