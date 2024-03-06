package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.Operator;
import cool.klass.model.meta.domain.api.value.ExpressionValue;

public interface OperatorCriteria extends Criteria
{
    @Nonnull
    Operator getOperator();

    @Nonnull
    ExpressionValue getSourceValue();

    @Nonnull
    ExpressionValue getTargetValue();

    @Override
    default void visit(CriteriaVisitor visitor)
    {
        visitor.visitOperator(this);
    }
}
