package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Enumeration;

public interface EnumerationUrlPathParameter extends UrlPathParameter
{
    @Override
    @Nonnull
    Enumeration getType();
}
