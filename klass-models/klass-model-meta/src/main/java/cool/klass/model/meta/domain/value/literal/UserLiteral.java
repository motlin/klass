package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

public final class UserLiteral extends LiteralValue
{
    private UserLiteral(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    @Override
    public void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitUserLiteral(this);
    }

    public static final class UserLiteralBuilder extends LiteralValueBuilder
    {
        public UserLiteralBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Nonnull
        @Override
        public UserLiteral build()
        {
            return new UserLiteral(this.elementContext, this.inferred);
        }
    }
}
