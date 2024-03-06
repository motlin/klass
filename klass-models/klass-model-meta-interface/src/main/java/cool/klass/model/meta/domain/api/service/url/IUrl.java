package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IElement;
import cool.klass.model.meta.domain.api.service.IService;
import cool.klass.model.meta.domain.api.service.IServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;

public interface IUrl extends IElement
{
    @Nonnull
    IServiceGroup getServiceGroup();

    ImmutableList<UrlPathSegment> getUrlPathSegments();

    ImmutableList<IUrlQueryParameter> getQueryParameters();

    ImmutableList<IUrlParameter> getUrlParameters();

    ImmutableList<IService> getServices();
}
