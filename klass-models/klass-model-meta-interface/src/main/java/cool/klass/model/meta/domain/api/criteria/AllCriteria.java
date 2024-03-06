package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface AllCriteria extends Criteria
{
    @Nonnull
    @Override
    default String getSourceCode()
    {
        return "all";
    }

    @Override
    default void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitAll(this);
    }
}
