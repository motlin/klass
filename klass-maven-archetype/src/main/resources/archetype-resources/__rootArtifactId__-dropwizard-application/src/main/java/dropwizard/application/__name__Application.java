package ${package}.dropwizard.application;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.service.klass.html.KlassHtmlResource;
import ${package}.graphql.runtime.wiring.${name}RuntimeWiringBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;
import io.liftwizard.dropwizard.bundle.httplogging.JerseyHttpLoggingBundle;
import io.liftwizard.servlet.logging.logstash.encoder.StructuredArgumentsLogstashEncoderLogger;
import io.liftwizard.servlet.logging.mdc.StructuredArgumentsMDCLogger;
import io.liftwizard.servlet.logging.typesafe.StructuredArguments;

public class ${name}Application
        extends Abstract${name}Application
{
    public static void main(String[] args) throws Exception
    {
        new ${name}Application().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<${name}Configuration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
    }

    @Override
    protected void initializeCommands(@Nonnull Bootstrap<${name}Configuration> bootstrap)
    {
        super.initializeCommands(bootstrap);
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<${name}Configuration> bootstrap)
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

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new ${name}RuntimeWiringBuilder()));

        /*
        bootstrap.addBundle(new MigrationsBundle<>()
        {
            @Override
            public DataSourceFactory getDataSourceFactory(${name}Configuration configuration)
            {
                return configuration.getNamedDataSourcesFactory().getNamedDataSourceFactoryByName("h2-tcp");
            }
        });
        */
    }

    @Override
    protected void registerJacksonModules(@Nonnull Environment environment)
    {
        super.registerJacksonModules(environment);

        environment.getObjectMapper().registerModule(new KlassMetaModelJacksonModule());
    }

    @Override
    public void run(
            @Nonnull ${name}Configuration configuration,
            @Nonnull Environment environment) throws Exception
    {
        ObjectMapper objectMapper = environment.getObjectMapper();
        DomainModel domainModel = configuration
                .getKlassFactory()
                .getDomainModelFactory()
                .createDomainModel(objectMapper);

        if (domainModel instanceof DomainModelWithSourceCode)
        {
            environment.jersey().register(new KlassHtmlResource((DomainModelWithSourceCode) domainModel));
        }

        super.run(configuration, environment);
    }
}
