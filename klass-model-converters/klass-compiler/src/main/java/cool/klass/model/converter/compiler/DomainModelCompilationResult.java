package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public class DomainModelCompilationResult
        extends AbstractCompilationResult
{
    private final DomainModel domainModel;

    public DomainModelCompilationResult(
            @Nonnull ImmutableList<SourceCode> sourceCodes,
            @Nonnull DomainModel domainModel)
    {
        super(sourceCodes);
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public DomainModel getDomainModel()
    {
        return this.domainModel;
    }
}
