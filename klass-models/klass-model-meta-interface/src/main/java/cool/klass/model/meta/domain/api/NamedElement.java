package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface NamedElement
        extends OrdinalElement
{
    @Nonnull
    String getName();
}
