package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;

public interface Url
        extends Element
{
    @Nonnull
    ServiceGroup getServiceGroup();

    ImmutableList<Element> getUrlPathSegments();

    ImmutableList<Parameter> getParameters();

    ImmutableList<Parameter> getQueryParameters();

    ImmutableList<Parameter> getPathParameters();

    ImmutableList<Service> getServices();

    default String getUrlString()
    {
        return this.getUrlPathSegments().makeString("/", "/", "");
    }
}
