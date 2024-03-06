package cool.klass.dropwizard.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.clock.ClockFactory;
import cool.klass.dropwizard.configuration.clock.fixed.FixedClockFactory;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.data.store.reladomo.ReladomoDataStoreFactory;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.dropwizard.configuration.domain.model.loader.compiler.DomainModelCompilerFactory;

public class KlassFactory
{
    private @NotNull @Valid ClockFactory       clockFactory       = new FixedClockFactory();
    private @NotNull @Valid DomainModelFactory domainModelFactory = new DomainModelCompilerFactory();
    private @NotNull @Valid DataStoreFactory   dataStoreFactory   = new ReladomoDataStoreFactory();

    @JsonProperty("clock")
    public ClockFactory getClockFactory()
    {
        return this.clockFactory;
    }

    @JsonProperty("clock")
    public void setClockFactory(ClockFactory clockFactory)
    {
        this.clockFactory = clockFactory;
    }

    @JsonProperty("domainModel")
    public DomainModelFactory getDomainModelFactory()
    {
        return this.domainModelFactory;
    }

    @JsonProperty("domainModel")
    public void setDomainModelFactory(DomainModelFactory domainModelFactory)
    {
        this.domainModelFactory = domainModelFactory;
    }

    @JsonProperty("dataStore")
    public DataStoreFactory getDataStoreFactory()
    {
        return this.dataStoreFactory;
    }

    @JsonProperty("dataStore")
    public void setDataStoreFactory(DataStoreFactory dataStoreFactory)
    {
        this.dataStoreFactory = dataStoreFactory;
    }
}
