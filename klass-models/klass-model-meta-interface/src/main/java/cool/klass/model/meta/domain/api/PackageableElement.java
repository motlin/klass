package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface PackageableElement
        extends NamedElement
{
    @Nonnull
    String getPackageName();

    @Nonnull
    default String getFullyQualifiedName()
    {
        return this.getPackageName() + "." + this.getName();
    }
}
