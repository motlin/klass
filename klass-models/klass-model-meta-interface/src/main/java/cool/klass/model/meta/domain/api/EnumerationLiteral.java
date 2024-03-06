package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EnumerationLiteral extends TypedElement
{
    @Nullable
    String getPrettyName();

    @Override
    @Nonnull
    Enumeration getType();
}
