package cool.klass.model.meta.domain.api.operator;

import javax.annotation.Nonnull;

public interface InOperator
        extends Operator
{
    @Override
    default void visit(@Nonnull OperatorVisitor visitor)
    {
        visitor.visitIn(this);
    }
}
