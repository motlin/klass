package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface ITypedElement<T extends Type> extends INamedElement
{
    @Nonnull
    T getType();
}
