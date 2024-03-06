package cool.klass.model.converter.compiler;

import java.util.Optional;

import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public record CompilationResult(
        ImmutableList<RootCompilerAnnotation> compilerAnnotations,
        Optional<DomainModelWithSourceCode> domainModelWithSourceCode)
{
}
