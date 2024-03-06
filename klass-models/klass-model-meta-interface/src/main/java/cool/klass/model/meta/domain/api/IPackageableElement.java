package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface IPackageableElement extends INamedElement
{
    @Nonnull
    String getPackageName();

    @Nonnull
    String getFullyQualifiedName();
}
