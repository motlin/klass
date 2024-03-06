package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface INamedElement extends IElement
{
    @Nonnull
    String getName();

    int getOrdinal();
}
