package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface BinaryCriteria extends ICriteria
{
    @Nonnull
    ICriteria getLeft();

    @Nonnull
    ICriteria getRight();
}
