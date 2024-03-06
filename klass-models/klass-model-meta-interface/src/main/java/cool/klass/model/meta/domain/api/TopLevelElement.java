package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface TopLevelElement
        extends PackageableElement
{
    void visit(TopLevelElementVisitor visitor);

    interface TopLevelElementBuilder
    {
        @Nonnull
        TopLevelElement getElement();
    }
}
