package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.IOperator;
import cool.klass.model.meta.domain.api.value.IExpressionValue;

public interface IOperatorCriteria extends ICriteria
{
    @Nonnull
    IOperator getOperator();

    @Nonnull
    IExpressionValue getSourceValue();

    @Nonnull
    IExpressionValue getTargetValue();

    @Override
    default void visit(CriteriaVisitor visitor)
    {
        visitor.visitOperator(this);
    }
}
