package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

public interface Element
{
    Optional<Element> getMacroElement();

    @Nonnull
    String getSourceCode();

    @Nonnull
    default String getSourceCodeWithInference()
    {
        return this.getSourceCode();
    }
}
