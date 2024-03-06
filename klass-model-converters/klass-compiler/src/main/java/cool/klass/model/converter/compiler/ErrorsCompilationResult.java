package cool.klass.model.converter.compiler;

import java.util.Objects;

import cool.klass.model.converter.compiler.error.RootCompilerError;
import org.eclipse.collections.api.list.ImmutableList;

public class ErrorsCompilationResult implements CompilationResult
{
    private final ImmutableList<RootCompilerError> compilerErrors;

    public ErrorsCompilationResult(ImmutableList<RootCompilerError> compilerErrors)
    {
        this.compilerErrors = Objects.requireNonNull(compilerErrors);
    }

    public ImmutableList<RootCompilerError> getCompilerErrors()
    {
        return this.compilerErrors;
    }
}
