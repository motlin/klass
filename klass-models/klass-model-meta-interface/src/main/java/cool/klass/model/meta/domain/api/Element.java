package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

public interface Element
{
    boolean isInferred();

    @Nonnull
    String getSourceCode();
}
