package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;

public interface Url extends Element
{
    @Nonnull
    ServiceGroup getServiceGroup();

    ImmutableList<UrlPathSegment> getUrlPathSegments();

    ImmutableList<UrlQueryParameter> getQueryParameters();

    ImmutableList<UrlParameter> getUrlParameters();

    ImmutableList<Service> getServices();
}
