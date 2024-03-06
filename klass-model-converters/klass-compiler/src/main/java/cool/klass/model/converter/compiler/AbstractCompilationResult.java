package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractCompilationResult
        implements CompilationResult
{
    @Nonnull
    private final ImmutableList<SourceCode> sourceCodes;

    protected AbstractCompilationResult(@Nonnull ImmutableList<SourceCode> sourceCodes)
    {
        this.sourceCodes = Objects.requireNonNull(sourceCodes);
    }

    @Override
    @Nonnull
    public ImmutableList<SourceCode> getSourceCodes()
    {
        return this.sourceCodes;
    }
}
