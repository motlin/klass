package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public class ErrorsCompilationResult
        implements CompilationResult
{
    @Nonnull
    private final ImmutableList<SourceCode>             sourceCodes;
    @Nonnull
    private final ImmutableList<RootCompilerAnnotation> compilerAnnotations;

    public ErrorsCompilationResult(
            @Nonnull ImmutableList<SourceCode> sourceCodes,
            @Nonnull ImmutableList<RootCompilerAnnotation> compilerAnnotations)
    {
        this.sourceCodes         = Objects.requireNonNull(sourceCodes);
        this.compilerAnnotations = Objects.requireNonNull(compilerAnnotations);
    }

    @Override
    @Nonnull
    public ImmutableList<SourceCode> getSourceCodes()
    {
        return this.sourceCodes;
    }

    @Nonnull
    public ImmutableList<RootCompilerAnnotation> getCompilerAnnotations()
    {
        return this.compilerAnnotations;
    }
}
