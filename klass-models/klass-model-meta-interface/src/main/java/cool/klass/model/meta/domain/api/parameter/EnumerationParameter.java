package cool.klass.model.meta.domain.api.parameter;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Enumeration;

public interface EnumerationParameter extends Parameter
{
    @Override
    @Nonnull
    Enumeration getType();
}
