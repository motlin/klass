package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IEnumeration;

public interface EnumerationUrlQueryParameter extends IUrlQueryParameter
{
    @Override
    @Nonnull
    IEnumeration getType();
}
