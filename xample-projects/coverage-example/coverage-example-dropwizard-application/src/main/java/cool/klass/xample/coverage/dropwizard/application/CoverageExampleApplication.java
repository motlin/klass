package cool.klass.xample.coverage.dropwizard.application;

import java.util.function.Consumer;

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
import io.liftwizard.servlet.logging.logstash.encoder.StructuredArgumentsLogstashEncoderLogger;
import io.liftwizard.servlet.logging.mdc.StructuredArgumentsMDCLogger;
import io.liftwizard.servlet.logging.typesafe.StructuredArguments;

public class CoverageExampleApplication
        extends AbstractCoverageExampleApplication
{
    public static void main(String[] args)
            throws Exception
    {
        new CoverageExampleApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initialize(bootstrap);
    }

    @Override
    protected void initializeCommands(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initializeCommands(bootstrap);
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        var mdcLogger = new StructuredArgumentsMDCLogger(bootstrap.getObjectMapper());
        var logstashLogger = new StructuredArgumentsLogstashEncoderLogger();

        Consumer<StructuredArguments> structuredLogger = structuredArguments ->
        {
            mdcLogger.accept(structuredArguments);
            logstashLogger.accept(structuredArguments);
        };

        bootstrap.addBundle(new JerseyHttpLoggingBundle(structuredLogger));

        bootstrap.addBundle(new KlassGraphQLBundle<>());

        bootstrap.addBundle(new MigrationsBundle<>()
        {
            @Override
            public DataSourceFactory getDataSourceFactory(CoverageExampleConfiguration configuration)
            {
                return configuration.getNamedDataSourcesFactory().getNamedDataSourceFactoryByName("h2-tcp");
            }
        });

        bootstrap.addBundle(new SPARedirectFilterBundle<>()
        {
            @Override
            public SPARedirectFilterFactory getSPARedirectFilterFactory(CoverageExampleConfiguration configuration)
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
