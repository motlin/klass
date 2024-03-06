package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.IMemberReferencePath;

public interface IEdgePointCriteria extends ICriteria
{
    @Nonnull
    IMemberReferencePath getMemberExpressionValue();

    @Override
    default void visit(CriteriaVisitor visitor)
    {
        visitor.visitEdgePoint(this);
    }
}
