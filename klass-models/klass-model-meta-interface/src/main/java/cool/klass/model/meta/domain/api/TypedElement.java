package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface TypedElement extends NamedElement
{
    @Nonnull
    Type getType();
}
