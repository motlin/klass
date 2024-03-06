package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.SourceCode;

public interface Element
{
    Optional<Element> getMacroElement();

    // TODO: Rename
    Optional<SourceCode> getSourceCodeObject();

    @Nonnull
    String getSourceCode();

    @Nonnull
    default String getSourceCodeWithInference()
    {
        return this.getSourceCode() + "\n";
    }
}
