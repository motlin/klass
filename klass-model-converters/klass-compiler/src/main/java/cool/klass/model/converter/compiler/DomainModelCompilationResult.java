package cool.klass.model.converter.compiler;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import org.eclipse.collections.api.list.ImmutableList;

public class DomainModelCompilationResult
        implements CompilationResult
{
    private final DomainModelWithSourceCode domainModel;

    public DomainModelCompilationResult(@Nonnull DomainModelWithSourceCode domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public DomainModelWithSourceCode getDomainModel()
    {
        return this.domainModel;
    }

    @Nonnull
    @Override
    public ImmutableList<SourceCode> getSourceCodes()
    {
        return this.domainModel.getSourceCodes();
    }
}
