package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Enumeration;

public interface EnumerationUrlQueryParameter extends UrlQueryParameter
{
    @Override
    @Nonnull
    Enumeration getType();
}
