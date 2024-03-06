package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.command.model.json.GenerateJsonModelCommand;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.httplogging.JerseyHttpLoggingBundle;
import io.liftwizard.servlet.logging.mdc.StructuredArgumentsMDCLogger;

public class KlassBootstrappedMetaModelApplication
        extends AbstractKlassBootstrappedMetaModelApplication
{
    public static void main(String[] args) throws Exception
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
        bootstrap.addCommand(new GenerateJsonModelCommand<>(this));
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<KlassBootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        var structuredLogger = new StructuredArgumentsMDCLogger(bootstrap.getObjectMapper());
        bootstrap.addBundle(new JerseyHttpLoggingBundle(structuredLogger));
    }

    @Override
    protected void registerJacksonModules(@Nonnull Environment environment)
    {
        super.registerJacksonModules(environment);

        environment.getObjectMapper().registerModule(new KlassMetaModelJacksonModule());
    }
}
