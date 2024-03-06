package cool.klass.model.meta.domain.api.operator;

import javax.annotation.Nonnull;

public interface EqualityOperator extends Operator
{
    @Override
    default void visit(@Nonnull OperatorVisitor visitor)
    {
        visitor.visitEquality(this);
    }
}
