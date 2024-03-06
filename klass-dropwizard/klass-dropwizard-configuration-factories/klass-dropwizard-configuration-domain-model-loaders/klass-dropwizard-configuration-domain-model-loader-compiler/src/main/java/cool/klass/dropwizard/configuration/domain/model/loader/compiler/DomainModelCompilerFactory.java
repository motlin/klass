package cool.klass.dropwizard.configuration.domain.model.loader.compiler;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.loader.DomainModelLoader;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.hibernate.validator.constraints.NotEmpty;

@JsonTypeName("compiler")
@AutoService(DomainModelFactory.class)
public class DomainModelCompilerFactory
        implements DomainModelFactory
{
    @NotEmpty
    private @Valid @NotNull List<String> sourcePackages = Arrays.asList("klass.model.meta.domain");

    private DomainModel domainModel;

    @Nonnull
    @Override
    public DomainModel createDomainModel()
    {
        if (this.domainModel != null)
        {
            return this.domainModel;
        }
        ImmutableList<String> klassSourcePackagesImmutable = Lists.immutable.withAll(this.sourcePackages);
        DomainModelLoader     domainModelLoader            = new DomainModelLoader(klassSourcePackagesImmutable);
        this.domainModel = domainModelLoader.load();
        return this.domainModel;
    }

    @JsonProperty
    public List<String> getSourcePackages()
    {
        return Lists.mutable.withAll(this.sourcePackages);
    }

    @JsonProperty
    public void setSourcePackages(List<String> sourcePackages)
    {
        this.sourcePackages = sourcePackages;
    }
}
