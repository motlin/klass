package cool.klass.dropwizard.configuration;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    private @Valid          DdlExecutorFactory       ddlExecutorFactory;
    private @Valid @NotNull List<AuthFilterFactory>  authFilterFactories;
    private @Valid @NotNull ObjectMapperFactory      objectMapperFactory      = new ObjectMapperFactory();
    private @Valid @NotNull EnabledFactory           bootstrapFactory         = new EnabledFactory();
    private @Valid @NotNull ReladomoFactory          reladomoFactory          = new ReladomoFactory();
    private @Valid @NotNull SampleDataFactory        sampleDataFactory        = new SampleDataFactory();
    private @Valid @NotNull EnabledFactory           configLoggingFactory     = new EnabledFactory();

    public KlassFactory getKlassFactory()
    {
        return this.klassFactory;
    }

    @Override
    public DataStoreFactory getDataStoreFactory()
    {
        return this.getKlassFactory().getDataStoreFactory();
    }

    @Override
    public DomainModelFactory getDomainModelFactory()
    {
        return this.getKlassFactory().getDomainModelFactory();
    }

    @JsonProperty
    public void setKlass(KlassFactory klassFactory)
    {
        this.klassFactory = klassFactory;
    }

    @Override
    public JerseyHttpLoggingFactory getJerseyHttpLoggingFactory()
    {
        return this.jerseyHttpLoggingFactory;
    }

    @JsonProperty
    public void setJerseyHttpLogging(JerseyHttpLoggingFactory jerseyHttpLoggingFactory)
    {
        this.jerseyHttpLoggingFactory = jerseyHttpLoggingFactory;
    }

    @Override
    public H2Factory getH2Factory()
    {
        return this.h2Factory;
    }

    @JsonProperty
    public void setH2(H2Factory h2Factory)
    {
        this.h2Factory = h2Factory;
    }

    @Override
    public CorsFactory getCorsFactory()
    {
        return this.corsFactory;
    }

    @JsonProperty
    public void setCors(CorsFactory corsFactory)
    {
        this.corsFactory = corsFactory;
    }

    @Override
    public DdlExecutorFactory getDdlExecutorFactory()
    {
        return this.ddlExecutorFactory;
    }

    @JsonProperty
    public void setDdlExecutor(DdlExecutorFactory ddlExecutorFactory)
    {
        this.ddlExecutorFactory = ddlExecutorFactory;
    }

    @Override
    public List<AuthFilterFactory> getAuthFilterFactories()
    {
        return this.authFilterFactories;
    }

    @JsonProperty
    public void setAuthFilters(List<AuthFilterFactory> authFilterFactories)
    {
        this.authFilterFactories = authFilterFactories;
    }

    @Override
    public ObjectMapperFactory getObjectMapperFactory()
    {
        return this.objectMapperFactory;
    }

    @JsonProperty
    public void setObjectMapper(ObjectMapperFactory objectMapperFactory)
    {
        this.objectMapperFactory = objectMapperFactory;
    }

    public EnabledFactory getBootstrapFactory()
    {
        return this.bootstrapFactory;
    }

    @JsonProperty
    public void setBootstrap(EnabledFactory bootstrapFactory)
    {
        this.bootstrapFactory = bootstrapFactory;
    }

    @Override
    public ReladomoFactory getReladomoFactory()
    {
        return this.reladomoFactory;
    }

    @JsonProperty
    public void setReladomo(ReladomoFactory reladomoFactory)
    {
        this.reladomoFactory = reladomoFactory;
    }

    @Override
    public SampleDataFactory getSampleDataFactory()
    {
        return this.sampleDataFactory;
    }

    @JsonProperty
    public void setSampleData(SampleDataFactory sampleDataFactory)
    {
        this.sampleDataFactory = sampleDataFactory;
    }

    public EnabledFactory getConfigLoggingFactory()
    {
        return this.configLoggingFactory;
    }

    @JsonProperty
    public void setConfigLogging(EnabledFactory configLoggingFactory)
    {
        this.configLoggingFactory = configLoggingFactory;
    }
}
