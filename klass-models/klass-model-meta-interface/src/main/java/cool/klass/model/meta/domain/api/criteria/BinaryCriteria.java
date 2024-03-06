package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface BinaryCriteria
        extends Criteria
{
    @Nonnull
    Criteria getLeft();

    @Nonnull
    Criteria getRight();
}
