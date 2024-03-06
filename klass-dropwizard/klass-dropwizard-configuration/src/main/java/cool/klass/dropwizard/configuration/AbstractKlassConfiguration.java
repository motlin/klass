package cool.klass.dropwizard.configuration;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactoryProvider;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactoryProvider;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactory;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactoryProvider;
import io.dropwizard.Configuration;
import io.liftwizard.dropwizard.configuration.auth.filter.AuthFilterFactory;
import io.liftwizard.dropwizard.configuration.auth.filter.AuthFilterFactoryProvider;
import io.liftwizard.dropwizard.configuration.clock.ClockFactory;
import io.liftwizard.dropwizard.configuration.clock.ClockFactoryProvider;
import io.liftwizard.dropwizard.configuration.config.logging.ConfigLoggingFactoryProvider;
import io.liftwizard.dropwizard.configuration.connectionmanager.ConnectionManagerProvider;
import io.liftwizard.dropwizard.configuration.connectionmanager.ConnectionManagersFactory;
import io.liftwizard.dropwizard.configuration.cors.CorsFactory;
import io.liftwizard.dropwizard.configuration.cors.CorsFactoryProvider;
import io.liftwizard.dropwizard.configuration.datasource.NamedDataSourceProvider;
import io.liftwizard.dropwizard.configuration.datasource.NamedDataSourcesFactory;
import io.liftwizard.dropwizard.configuration.ddl.executor.DdlExecutorFactory;
import io.liftwizard.dropwizard.configuration.ddl.executor.DdlExecutorFactoryProvider;
import io.liftwizard.dropwizard.configuration.enabled.EnabledFactory;
import io.liftwizard.dropwizard.configuration.h2.H2Factory;
import io.liftwizard.dropwizard.configuration.h2.H2FactoryProvider;
import io.liftwizard.dropwizard.configuration.http.logging.JerseyHttpLoggingFactory;
import io.liftwizard.dropwizard.configuration.http.logging.JerseyHttpLoggingFactoryProvider;
import io.liftwizard.dropwizard.configuration.object.mapper.ObjectMapperFactory;
import io.liftwizard.dropwizard.configuration.object.mapper.ObjectMapperFactoryProvider;
import io.liftwizard.dropwizard.configuration.reladomo.ReladomoFactory;
import io.liftwizard.dropwizard.configuration.reladomo.ReladomoFactoryProvider;
import io.liftwizard.dropwizard.configuration.system.properties.SystemPropertiesFactory;
import io.liftwizard.dropwizard.configuration.system.properties.SystemPropertiesFactoryProvider;
import io.liftwizard.dropwizard.configuration.uuid.UUIDSupplierFactory;
import io.liftwizard.dropwizard.configuration.uuid.UUIDSupplierFactoryProvider;

@JsonPropertyOrder({
        "server",
        "logging",
        "metrics",
        "klass",
        "configLogging",
        "objectMapper",
        "cors",
        "authFilters",
        "jerseyHttpLogging",
        "h2",
        "dataSources",
        "ddlExecutors",
        "reladomo",
        "connectionManagers",
        "sampleData",
        "bootstrap",
})
public class AbstractKlassConfiguration
        extends Configuration
        implements ConfigLoggingFactoryProvider,
        CorsFactoryProvider,
        AuthFilterFactoryProvider,
        ObjectMapperFactoryProvider,
        JerseyHttpLoggingFactoryProvider,
        H2FactoryProvider,
        DdlExecutorFactoryProvider,
        ReladomoFactoryProvider,
        SampleDataFactoryProvider,
        DataStoreFactoryProvider,
        DomainModelFactoryProvider,
        UUIDSupplierFactoryProvider,
        ClockFactoryProvider,
        NamedDataSourceProvider,
        ConnectionManagerProvider,
        SystemPropertiesFactoryProvider
{
    // General
    private @Valid          KlassFactory            klassFactory;
    private @Valid @NotNull SystemPropertiesFactory systemPropertiesFactory = new SystemPropertiesFactory();

    // Services
    private @Valid @NotNull EnabledFactory           configLoggingFactory     = new EnabledFactory();
    private @Valid @NotNull ObjectMapperFactory      objectMapperFactory      = new ObjectMapperFactory();
    private @Valid @NotNull CorsFactory              corsFactory              = new CorsFactory();
    private @Valid @NotNull List<AuthFilterFactory>  authFilterFactories      = Arrays.asList();
    private @Valid @NotNull JerseyHttpLoggingFactory jerseyHttpLoggingFactory = new JerseyHttpLoggingFactory();

    // Data
    private @Valid          H2Factory                h2Factory;
    private @Valid @NotNull List<DdlExecutorFactory> ddlExecutorFactories = Arrays.asList();
    private @Valid @NotNull ReladomoFactory          reladomoFactory      = new ReladomoFactory();
    private @Valid @NotNull SampleDataFactory        sampleDataFactory    = new SampleDataFactory();
    private @Valid @NotNull EnabledFactory           bootstrapFactory     = new EnabledFactory();

    @JsonUnwrapped
    private @Valid @NotNull NamedDataSourcesFactory namedDataSourcesFactory =
            new NamedDataSourcesFactory();

    @JsonUnwrapped
    private @Valid @NotNull ConnectionManagersFactory connectionManagersFactory =
            new ConnectionManagersFactory();

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
    @JsonProperty("configLogging")
    public EnabledFactory getConfigLoggingFactory()
    {
        return this.configLoggingFactory;
    }

    @JsonProperty("configLogging")
    public void setConfigLoggingFactory(EnabledFactory configLoggingFactory)
    {
        this.configLoggingFactory = configLoggingFactory;
    }

    @Override
    @JsonProperty("objectMapper")
    public ObjectMapperFactory getObjectMapperFactory()
    {
        return this.objectMapperFactory;
    }

    @JsonProperty("objectMapper")
    public void setObjectMapperFactory(ObjectMapperFactory objectMapperFactory)
    {
        this.objectMapperFactory = objectMapperFactory;
    }

    @Override
    @JsonProperty("jerseyHttpLogging")
    public JerseyHttpLoggingFactory getJerseyHttpLoggingFactory()
    {
        return this.jerseyHttpLoggingFactory;
    }

    @JsonProperty("jerseyHttpLogging")
    public void setJerseyHttpLoggingFactory(JerseyHttpLoggingFactory jerseyHttpLoggingFactory)
    {
        this.jerseyHttpLoggingFactory = jerseyHttpLoggingFactory;
    }

    // TODO: Error messages contain the word factory because of the field name
    // config-test.json5 has the following errors:
    //   * h2Factory.tcpPort may not be null
    //   * h2Factory.webPort may not be null
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

    @JsonProperty("dataSources")
    @JsonUnwrapped
    @Override
    public NamedDataSourcesFactory getNamedDataSourcesFactory()
    {
        return this.namedDataSourcesFactory;
    }

    @JsonProperty("dataSources")
    @JsonUnwrapped
    public void setNamedDataSourcesFactory(NamedDataSourcesFactory namedDataSourcesFactory)
    {
        this.namedDataSourcesFactory = namedDataSourcesFactory;
    }

    @JsonProperty("connectionManagers")
    @JsonUnwrapped
    @Override
    public ConnectionManagersFactory getConnectionManagersFactory()
    {
        return this.connectionManagersFactory;
    }

    @JsonProperty("connectionManagers")
    @JsonUnwrapped
    public void setConnectionManagersFactory(ConnectionManagersFactory connectionManagersFactory)
    {
        this.connectionManagersFactory = connectionManagersFactory;
    }

    @Override
    @JsonProperty("ddlExecutors")
    public List<DdlExecutorFactory> getDdlExecutorFactories()
    {
        return this.ddlExecutorFactories;
    }

    @JsonProperty("ddlExecutors")
    public void setDdlExecutorFactories(List<DdlExecutorFactory> ddlExecutorFactories)
    {
        this.ddlExecutorFactories = ddlExecutorFactories;
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
    @JsonProperty("systemProperties")
    public SystemPropertiesFactory getSystemPropertiesFactory()
    {
        return this.systemPropertiesFactory;
    }

    @JsonProperty("systemProperties")
    public void setSystemPropertiesFactory(SystemPropertiesFactory systemPropertiesFactory)
    {
        this.systemPropertiesFactory = systemPropertiesFactory;
    }

    @Override
    @JsonIgnore
    public DataStoreFactory getDataStoreFactory()
    {
        return this.klassFactory.getDataStoreFactory();
    }

    @Override
    @JsonIgnore
    public DomainModelFactory getDomainModelFactory()
    {
        return this.klassFactory.getDomainModelFactory();
    }

    @JsonIgnore
    @Override
    public UUIDSupplierFactory getUuidSupplierFactory()
    {
        return this.getDataStoreFactory().getUuidFactory();
    }

    @JsonIgnore
    @Override
    public ClockFactory getClockFactory()
    {
        return this.klassFactory.getClockFactory();
    }
}
