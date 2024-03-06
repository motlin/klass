package cool.klass.model.meta.domain.api.service;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.service.url.Url;
import org.eclipse.collections.api.list.ImmutableList;

public interface ServiceGroup extends PackageableElement
{
    @Nonnull
    Klass getKlass();

    @Nonnull
    ImmutableList<Url> getUrls();
}
