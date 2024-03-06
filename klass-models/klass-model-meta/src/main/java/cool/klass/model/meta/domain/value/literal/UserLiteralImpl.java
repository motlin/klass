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

    public static final class UserLiteralBuilder extends AbstractLiteralValueBuilder<UserLiteralImpl>
    {
        public UserLiteralBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Override
        @Nonnull
        protected UserLiteralImpl buildUnsafe()
        {
            return new UserLiteralImpl(this.elementContext, this.inferred);
        }
    }
}
