package cool.klass.model.meta.domain.api.parameter;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IEnumeration;

public interface IEnumerationParameter extends IParameter
{
    @Override
    @Nonnull
    IEnumeration getType();
}
