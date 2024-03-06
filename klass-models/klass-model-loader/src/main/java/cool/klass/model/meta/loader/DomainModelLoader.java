package cool.klass.model.meta.loader;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;

public interface DomainModelLoader
{
    @Nonnull
    DomainModel load();
}
