package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.bundle.graphql.KlassGraphQLBundle;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.httplogging.JerseyHttpLoggingBundle;
import io.liftwizard.servlet.bundle.spa.SPARedirectFilterBundle;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactory;
import io.liftwizard.servlet.logging.mdc.StructuredArgumentsMDCLogger;

public class KlassBootstrappedMetaModelApplication
        extends AbstractKlassBootstrappedMetaModelApplication
{
    public static void main(String[] args)
            throws Exception
    {
        new KlassBootstrappedMetaModelApplication().run(args);
    }

    @Override
    public Class<KlassBootstrappedMetaModelConfiguration> getConfigurationClass()
    {
        return super.getConfigurationClass();
    }

    @Override
    protected void initializeCommands(@Nonnull Bootstrap<KlassBootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initializeCommands(bootstrap);
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<KlassBootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        var structuredLogger = new StructuredArgumentsMDCLogger(bootstrap.getObjectMapper());
        bootstrap.addBundle(new JerseyHttpLoggingBundle(structuredLogger));
        bootstrap.addBundle(new KlassGraphQLBundle<>());

        bootstrap.addBundle(new MigrationsBundle<>()
        {
            @Override
            public DataSourceFactory getDataSourceFactory(KlassBootstrappedMetaModelConfiguration configuration)
            {
                return configuration.getNamedDataSourcesFactory().getNamedDataSourceFactoryByName("h2-tcp");
            }
        });

        bootstrap.addBundle(new SPARedirectFilterBundle<>()
        {
            @Override
            public SPARedirectFilterFactory getSPARedirectFilterFactory(KlassBootstrappedMetaModelConfiguration configuration)
            {
                return configuration.getSPARedirectFilterFactory();
            }
        });
    }

    @Override
    protected void registerJacksonModules(@Nonnull Environment environment)
    {
        super.registerJacksonModules(environment);

        environment.getObjectMapper().registerModule(new KlassMetaModelJacksonModule());
    }
}
