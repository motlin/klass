package cool.klass.model.meta.domain.api.source;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;

public interface ElementWithSourceCode
        extends Element
{
    @Nonnull
    SourceCode getSourceCodeObject();
}
