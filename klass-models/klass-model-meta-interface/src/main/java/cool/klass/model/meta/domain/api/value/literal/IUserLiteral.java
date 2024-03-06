package cool.klass.model.meta.domain.api.value.literal;

import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;

public interface IUserLiteral extends ILiteralValue
{
    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitUserLiteral(this);
    }
}
