package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;

public class AbstractCompilationResult
        implements CompilationResult
{
    @Nonnull
    private final ImmutableList<CompilationUnit> compilationUnits;

    public AbstractCompilationResult(@Nonnull ImmutableList<CompilationUnit> compilationUnits)
    {
        this.compilationUnits = Objects.requireNonNull(compilationUnits);
    }

    @Override
    public ImmutableList<CompilationUnit> getCompilationUnits()
    {
        return this.compilationUnits;
    }
}
