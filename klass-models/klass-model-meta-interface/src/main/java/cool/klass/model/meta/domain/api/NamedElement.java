package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface NamedElement extends Element
{
    @Nonnull
    String getName();

    int getOrdinal();
}
