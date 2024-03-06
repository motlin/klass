package cool.klass.model.meta.domain.api.source;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.TopLevelElement;

public interface TopLevelElementWithSourceCode
        extends TopLevelElement, PackageableElementWithSourceCode
{
    interface TopLevelElementBuilderWithSourceCode
            extends TopLevelElementBuilder
    {
        @Override
        @Nonnull
        TopLevelElementWithSourceCode getElement();
    }
}
