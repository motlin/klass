package cool.klass.model.converter.compiler;

import java.util.Objects;

import cool.klass.model.meta.domain.api.DomainModel;

public class DomainModelCompilationResult implements CompilationResult
{
    private final DomainModel domainModel;

    public DomainModelCompilationResult(DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public DomainModel getDomainModel()
    {
        return this.domainModel;
    }
}
