package cool.klass.model.converter.compiler;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public interface CompilationResult
{
    @Nonnull
    ImmutableList<SourceCode> getSourceCodes();
}
