package cool.klass.model.converter.compiler;

import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;

public record DomainModelCompilationResult(DomainModelWithSourceCode domainModel)
        implements CompilationResult
{
}
