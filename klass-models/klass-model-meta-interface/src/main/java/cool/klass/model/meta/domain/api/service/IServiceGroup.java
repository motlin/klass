package cool.klass.model.meta.domain.api.service;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IKlass;
import cool.klass.model.meta.domain.api.IPackageableElement;
import cool.klass.model.meta.domain.api.service.url.IUrl;
import org.eclipse.collections.api.list.ImmutableList;

public interface IServiceGroup extends IPackageableElement
{
    @Nonnull
    IKlass getKlass();

    @Nonnull
    ImmutableList<IUrl> getUrls();
}
