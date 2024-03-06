package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

public interface EnumerationLiteral
        extends TypedElement
{
    @Nonnull
    Optional<String> getDeclaredPrettyName();

    @Nonnull
    default String getPrettyName()
    {
        return this.getDeclaredPrettyName().orElseGet(this::getName);
    }

    @Override
    @Nonnull
    Enumeration getType();
}
