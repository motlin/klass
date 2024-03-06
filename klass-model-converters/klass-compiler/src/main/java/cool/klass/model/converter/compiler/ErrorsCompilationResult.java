package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public class ErrorsCompilationResult
        implements CompilationResult
{
    @Nonnull
    private final ImmutableList<SourceCode>        sourceCodes;
    @Nonnull
    private final ImmutableList<RootCompilerError> compilerErrors;

    public ErrorsCompilationResult(
            @Nonnull ImmutableList<SourceCode> sourceCodes,
            @Nonnull ImmutableList<RootCompilerError> compilerErrors)
    {
        this.sourceCodes    = Objects.requireNonNull(sourceCodes);
        this.compilerErrors = Objects.requireNonNull(compilerErrors);
    }

    @Override
    @Nonnull
    public ImmutableList<SourceCode> getSourceCodes()
    {
        return this.sourceCodes;
    }

    @Nonnull
    public ImmutableList<RootCompilerError> getCompilerErrors()
    {
        return this.compilerErrors;
    }
}
