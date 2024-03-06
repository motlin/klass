package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface IOrCriteria extends BinaryCriteria
{
    @Override
    default void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitOr(this);
    }
}
