package cool.klass.model.meta.domain.api.operator;

import javax.annotation.Nonnull;

public interface StringOperator extends Operator
{
    @Override
    default void visit(@Nonnull OperatorVisitor visitor)
    {
        visitor.visitString(this);
    }
}
