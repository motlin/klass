package cool.klass.dropwizard.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.sql.DataSource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactoryProvider;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactoryProvider;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactory;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactoryProvider;
import com.liftwizard.dropwizard.configuration.auth.filter.AuthFilterFactory;
import com.liftwizard.dropwizard.configuration.auth.filter.AuthFilterFactoryProvider;
import com.liftwizard.dropwizard.configuration.clock.ClockFactory;
import com.liftwizard.dropwizard.configuration.clock.ClockFactoryProvider;
import com.liftwizard.dropwizard.configuration.config.logging.ConfigLoggingFactoryProvider;
import com.liftwizard.dropwizard.configuration.connectionmanager.ConnectionManagerConfiguration;
import com.liftwizard.dropwizard.configuration.connectionmanager.ConnectionManagerFactory;
import com.liftwizard.dropwizard.configuration.connectionmanager.ConnectionManagerFactoryProvider;
import com.liftwizard.dropwizard.configuration.cors.CorsFactory;
import com.liftwizard.dropwizard.configuration.cors.CorsFactoryProvider;
import com.liftwizard.dropwizard.configuration.datasource.NamedDataSourceConfiguration;
import com.liftwizard.dropwizard.configuration.datasource.NamedDataSourceProvider;
import com.liftwizard.dropwizard.configuration.ddl.executor.DdlExecutorFactory;
import com.liftwizard.dropwizard.configuration.ddl.executor.DdlExecutorFactoryProvider;
import com.liftwizard.dropwizard.configuration.enabled.EnabledFactory;
import com.liftwizard.dropwizard.configuration.h2.H2Factory;
import com.liftwizard.dropwizard.configuration.h2.H2FactoryProvider;
import com.liftwizard.dropwizard.configuration.http.logging.JerseyHttpLoggingFactory;
import com.liftwizard.dropwizard.configuration.http.logging.JerseyHttpLoggingFactoryProvider;
import com.liftwizard.dropwizard.configuration.object.mapper.ObjectMapperFactory;
import com.liftwizard.dropwizard.configuration.object.mapper.ObjectMapperFactoryProvider;
import com.liftwizard.dropwizard.configuration.reladomo.ReladomoFactory;
import com.liftwizard.dropwizard.configuration.reladomo.ReladomoFactoryProvider;
import com.liftwizard.dropwizard.configuration.uuid.UUIDSupplierFactory;
import com.liftwizard.dropwizard.configuration.uuid.UUIDSupplierFactoryProvider;
import com.liftwizard.dropwizard.db.NamedDataSourceFactory;
import io.dropwizard.Configuration;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MapIterable;

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
                           "bootstrap"
                   })
public class AbstractKlassConfiguration
        extends Configuration
        implements JerseyHttpLoggingFactoryProvider,
        H2FactoryProvider,
        CorsFactoryProvider,
        DdlExecutorFactoryProvider,
        AuthFilterFactoryProvider,
        ObjectMapperFactoryProvider,
        ReladomoFactoryProvider,
        SampleDataFactoryProvider,
        ConfigLoggingFactoryProvider,
        DataStoreFactoryProvider,
        DomainModelFactoryProvider,
        UUIDSupplierFactoryProvider,
        ClockFactoryProvider,
        NamedDataSourceProvider,
        ConnectionManagerFactoryProvider
{
    // General
    private @Valid @NotNull KlassFactory   klassFactory         = new KlassFactory();
    private @Valid @NotNull EnabledFactory configLoggingFactory = new EnabledFactory();

    // Services
    private @Valid @NotNull ObjectMapperFactory      objectMapperFactory      = new ObjectMapperFactory();
    private @Valid @NotNull CorsFactory              corsFactory              = new CorsFactory();
    private @Valid @NotNull List<AuthFilterFactory>  authFilterFactories      = Arrays.asList();
    private @Valid @NotNull JerseyHttpLoggingFactory jerseyHttpLoggingFactory = new JerseyHttpLoggingFactory();

    // Data
    private @Valid @NotNull H2Factory                h2Factory            = new H2Factory();
    private @Valid @NotNull List<DdlExecutorFactory> ddlExecutorFactories = Arrays.asList();
    private @Valid @NotNull ReladomoFactory          reladomoFactory      = new ReladomoFactory();
    private @Valid @NotNull SampleDataFactory        sampleDataFactory    = new SampleDataFactory();
    private @Valid @NotNull EnabledFactory           bootstrapFactory     = new EnabledFactory();

    private @Valid @NotNull NamedDataSourceConfiguration   namedDataSourceConfiguration   =
            new NamedDataSourceConfiguration();
    private @Valid @NotNull ConnectionManagerConfiguration connectionManagerConfiguration =
            new ConnectionManagerConfiguration();

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
    public void setConfigLogging(EnabledFactory configLoggingFactory)
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
    public void setObjectMapper(ObjectMapperFactory objectMapperFactory)
    {
        this.objectMapperFactory = objectMapperFactory;
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
    public void initializeDataSources(
            @Nonnull MetricRegistry metricRegistry,
            @Nonnull LifecycleEnvironment lifecycle)
    {
        this.namedDataSourceConfiguration.initializeDataSources(metricRegistry, lifecycle);
    }

    @Override
    @JsonProperty("dataSources")
    public List<NamedDataSourceFactory> getNamedDataSourceFactories()
    {
        return this.namedDataSourceConfiguration.getNamedDataSourceFactories();
    }

    @JsonProperty("dataSources")
    public void setNamedDataSourceFactories(List<NamedDataSourceFactory> namedDataSourceFactories)
    {
        this.namedDataSourceConfiguration.setNamedDataSourceFactories(namedDataSourceFactories);
    }

    @Override
    @JsonIgnore
    public DataSource getDataSourceByName(@Nonnull String name)
    {
        return this.namedDataSourceConfiguration.getDataSourceByName(name);
    }

    @Override
    @JsonIgnore
    public MapIterable<String, ManagedDataSource> getDataSourcesByName()
    {
        return this.namedDataSourceConfiguration.getDataSourcesByName();
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
    public void initializeConnectionManagers(@Nonnull MapIterable<String, ManagedDataSource> dataSourcesByName)
    {
        this.connectionManagerConfiguration.initializeConnectionManagers(dataSourcesByName);
    }

    @Override
    @JsonProperty("connectionManagers")
    public List<ConnectionManagerFactory> getConnectionManagerFactories()
    {
        return this.connectionManagerConfiguration.getConnectionManagerFactories();
    }

    @JsonProperty("connectionManagers")
    public void setConnectionManagerFactories(List<ConnectionManagerFactory> connectionManagerFactories)
    {
        this.connectionManagerConfiguration.setConnectionManagerFactories(connectionManagerFactories);
    }

    @JsonIgnore
    @Override
    public SourcelessConnectionManager getConnectionManagerByName(@Nonnull String name)
    {
        SourcelessConnectionManager sourcelessConnectionManager =
                this.connectionManagerConfiguration.getConnectionManagerByName(name);
        return Objects.requireNonNull(
                sourcelessConnectionManager,
                () -> String.format("Could not find connection manager with name %s. Valid choices are %s",
                        name,
                        this.connectionManagerConfiguration.getConnectionManagersByName().keysView()));
    }

    @JsonIgnore
    @Override
    public ImmutableMap<String, SourcelessConnectionManager> getConnectionManagersByName()
    {
        return this.connectionManagerConfiguration.getConnectionManagersByName();
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
