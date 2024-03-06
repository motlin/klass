package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface AllCriteria
        extends Criteria
{
    @Override
    default void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitAll(this);
    }
}
