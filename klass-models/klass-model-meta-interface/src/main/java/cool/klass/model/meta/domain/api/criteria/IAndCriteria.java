package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface IAndCriteria extends BinaryCriteria
{
    @Override
    default void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitAnd(this);
    }
}
