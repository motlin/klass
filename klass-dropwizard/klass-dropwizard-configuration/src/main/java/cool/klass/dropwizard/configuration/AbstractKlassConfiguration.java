package cool.klass.dropwizard.configuration;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactory;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactoryProvider;
import cool.klass.dropwizard.configuration.cors.CorsFactory;
import cool.klass.dropwizard.configuration.cors.CorsFactoryProvider;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.ddl.executor.DdlExecutorFactory;
import cool.klass.dropwizard.configuration.ddl.executor.DdlExecutorFactoryProvider;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.dropwizard.configuration.h2.H2Factory;
import cool.klass.dropwizard.configuration.h2.H2FactoryProvider;
import cool.klass.dropwizard.configuration.http.logging.JerseyHttpLoggingFactory;
import cool.klass.dropwizard.configuration.http.logging.JerseyHttpLoggingFactoryProvider;
import cool.klass.dropwizard.configuration.object.mapper.ObjectMapperFactory;
import cool.klass.dropwizard.configuration.object.mapper.ObjectMapperFactoryProvider;
import cool.klass.dropwizard.configuration.reladomo.ReladomoFactory;
import cool.klass.dropwizard.configuration.reladomo.ReladomoFactoryProvider;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactory;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactoryProvider;
import io.dropwizard.Configuration;

public class AbstractKlassConfiguration extends Configuration
        implements JerseyHttpLoggingFactoryProvider, H2FactoryProvider, CorsFactoryProvider, DdlExecutorFactoryProvider, AuthFilterFactoryProvider, ObjectMapperFactoryProvider, ReladomoFactoryProvider, SampleDataFactoryProvider
{
    private @Valid @NotNull KlassFactory             klassFactory;
    private @Valid @NotNull JerseyHttpLoggingFactory jerseyHttpLoggingFactory = new JerseyHttpLoggingFactory();
    private @Valid @NotNull H2Factory                h2Factory                = new H2Factory();
    private @Valid @NotNull CorsFactory              corsFactory              = new CorsFactory();
    private @Valid @NotNull DdlExecutorFactory       ddlExecutorFactory       = new DdlExecutorFactory();
    private @Valid @NotNull List<AuthFilterFactory>  authFilterFactories;
    private @Valid @NotNull ObjectMapperFactory      objectMapperFactory      = new ObjectMapperFactory();
    private @Valid @NotNull EnabledFactory           bootstrapFactory         = new EnabledFactory();
    private @Valid @NotNull ReladomoFactory          reladomoFactory          = new ReladomoFactory();
    private @Valid @NotNull SampleDataFactory        sampleDataFactory        = new SampleDataFactory();
    private @Valid @NotNull EnabledFactory           configLoggingFactory     = new EnabledFactory();

    @Override
    @JsonIgnore
    public DataStoreFactory getDataStoreFactory()
    {
        return this.getKlassFactory().getDataStoreFactory();
    }

    @Override
    @JsonIgnore
    public DomainModelFactory getDomainModelFactory()
    {
        return this.getKlassFactory().getDomainModelFactory();
    }

    @JsonProperty("klass")
    public KlassFactory getKlassFactory()
    {
        return this.klassFactory;
    }

    @JsonProperty("klass")
    public void setKlass(KlassFactory klassFactory)
    {
        this.klassFactory = klassFactory;
    }

    @Override
    @JsonProperty("jerseyHttpLogging")
    public JerseyHttpLoggingFactory getJerseyHttpLoggingFactory()
    {
        return this.jerseyHttpLoggingFactory;
    }

    @JsonProperty("jerseyHttpLogging")
    public void setJerseyHttpLogging(JerseyHttpLoggingFactory jerseyHttpLoggingFactory)
    {
        this.jerseyHttpLoggingFactory = jerseyHttpLoggingFactory;
    }

    @Override
    @JsonProperty("h2")
    public H2Factory getH2Factory()
    {
        return this.h2Factory;
    }

    @JsonProperty("h2")
    public void setH2(H2Factory h2Factory)
    {
        this.h2Factory = h2Factory;
    }

    @Override
    @JsonProperty("cors")
    public CorsFactory getCorsFactory()
    {
        return this.corsFactory;
    }

    @JsonProperty("cors")
    public void setCors(CorsFactory corsFactory)
    {
        this.corsFactory = corsFactory;
    }

    @Override
    @JsonProperty("ddlExecutor")
    public DdlExecutorFactory getDdlExecutorFactory()
    {
        return this.ddlExecutorFactory;
    }

    @JsonProperty("ddlExecutor")
    public void setDdlExecutor(DdlExecutorFactory ddlExecutorFactory)
    {
        this.ddlExecutorFactory = ddlExecutorFactory;
    }

    @Override
    @JsonProperty("authFilters")
    public List<AuthFilterFactory> getAuthFilterFactories()
    {
        return this.authFilterFactories;
    }

    @JsonProperty("authFilters")
    public void setAuthFilters(List<AuthFilterFactory> authFilterFactories)
    {
        this.authFilterFactories = authFilterFactories;
    }

    @Override
    @JsonProperty("objectMapper")
    public ObjectMapperFactory getObjectMapperFactory()
    {
        return this.objectMapperFactory;
    }

    @JsonProperty("objectMapper")
    public void setObjectMapper(ObjectMapperFactory objectMapperFactory)
    {
        this.objectMapperFactory = objectMapperFactory;
    }

    @JsonProperty("bootstrap")
    public EnabledFactory getBootstrapFactory()
    {
        return this.bootstrapFactory;
    }

    @JsonProperty("bootstrap")
    public void setBootstrap(EnabledFactory bootstrapFactory)
    {
        this.bootstrapFactory = bootstrapFactory;
    }

    @Override
    @JsonProperty("reladomo")
    public ReladomoFactory getReladomoFactory()
    {
        return this.reladomoFactory;
    }

    @JsonProperty("reladomo")
    public void setReladomo(ReladomoFactory reladomoFactory)
    {
        this.reladomoFactory = reladomoFactory;
    }

    @Override
    @JsonProperty("sampleData")
    public SampleDataFactory getSampleDataFactory()
    {
        return this.sampleDataFactory;
    }

    @JsonProperty("sampleData")
    public void setSampleData(SampleDataFactory sampleDataFactory)
    {
        this.sampleDataFactory = sampleDataFactory;
    }

    @JsonProperty("configLogging")
    public EnabledFactory getConfigLoggingFactory()
    {
        return this.configLoggingFactory;
    }

    @JsonProperty("configLogging")
    public void setConfigLogging(EnabledFactory configLoggingFactory)
    {
        this.configLoggingFactory = configLoggingFactory;
    }
}
