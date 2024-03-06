package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public class ErrorsCompilationResult
        extends AbstractCompilationResult
{
    @Nonnull
    private final ImmutableList<RootCompilerError> compilerErrors;

    public ErrorsCompilationResult(
            @Nonnull ImmutableList<SourceCode> sourceCodes,
            @Nonnull ImmutableList<RootCompilerError> compilerErrors)
    {
        super(sourceCodes);
        this.compilerErrors = Objects.requireNonNull(compilerErrors);
    }

    public ImmutableList<RootCompilerError> getCompilerErrors()
    {
        return this.compilerErrors;
    }
}
