package cool.klass.model.meta.domain.api.value.literal;

import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;

public interface UserLiteral extends LiteralValue
{
    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitUserLiteral(this);
    }
}
