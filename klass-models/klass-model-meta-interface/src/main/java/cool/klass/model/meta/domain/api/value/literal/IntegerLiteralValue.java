package cool.klass.model.meta.domain.api.value.literal;

import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;

public interface IntegerLiteralValue extends LiteralValue
{
    int getValue();

    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitIntegerLiteral(this);
    }
}
