package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.api.PackageableElement;

// TODO: Delete this interface
public interface TopLevelElement
{
    String getName();

    String getPackageName();

    interface TopLevelElementBuilder
    {
        PackageableElement getElement();
    }
}
