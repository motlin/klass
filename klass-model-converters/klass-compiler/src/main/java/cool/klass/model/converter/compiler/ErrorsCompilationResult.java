package cool.klass.model.converter.compiler;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import org.eclipse.collections.api.list.ImmutableList;

public record ErrorsCompilationResult(@Nonnull ImmutableList<RootCompilerAnnotation> compilerAnnotations)
        implements CompilationResult
{
}
