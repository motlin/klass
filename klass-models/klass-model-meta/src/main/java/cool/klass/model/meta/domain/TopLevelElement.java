package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PackageableElement;

// TODO: Delete this interface
public interface TopLevelElement
{
    @Nonnull
    String getName();

    @Nonnull
    String getPackageName();

    interface TopLevelElementBuilder
    {
        PackageableElement getElement();
    }
}
