package cool.klass.model.converter.compiler;

public sealed interface CompilationResult
        permits DomainModelCompilationResult,
        ErrorsCompilationResult
{
}
