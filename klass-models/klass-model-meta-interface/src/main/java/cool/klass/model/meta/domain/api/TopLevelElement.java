package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface TopLevelElement extends Element
{
    @Nonnull
    String getName();

    @Nonnull
    String getPackageName();

    void visit(TopLevelElementVisitor visitor);

    interface TopLevelElementBuilder
    {
        @Nonnull
        PackageableElement getElement();
    }
}
