package cool.klass.dropwizard.configuration.domain.model.loader.compiler;

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

@JsonTypeName("compiler")
@AutoService(DomainModelFactory.class)
public class DomainModelCompilerFactory implements DomainModelFactory
{
    // TODO: Add @NotEmpty validation
    // javax.validation.UnexpectedTypeException: HV000030: No validator could be found for constraint 'javax.validation.constraints.NotEmpty' validating type 'java.util.List<java.lang.String>'. Check configuration for 'sourcePackages'
    private @Valid @NotNull List<String> sourcePackages;

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
    public void setSourcePackages(List<String> sourcePackages)
    {
        this.sourcePackages = sourcePackages;
    }
}
