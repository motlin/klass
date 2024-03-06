package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.PackageableElement;

// TODO: Delete this interface
public interface TopLevelElement extends Element
{
    @Nonnull
    String getName();

    @Nonnull
    String getPackageName();

    interface TopLevelElementBuilder
    {
        @Nonnull
        PackageableElement getElement();
    }
}
