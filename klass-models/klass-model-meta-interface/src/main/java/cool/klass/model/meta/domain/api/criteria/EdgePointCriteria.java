package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.MemberReferencePath;

public interface EdgePointCriteria extends Criteria
{
    @Nonnull
    MemberReferencePath getMemberExpressionValue();

    @Override
    default void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitEdgePoint(this);
    }
}
