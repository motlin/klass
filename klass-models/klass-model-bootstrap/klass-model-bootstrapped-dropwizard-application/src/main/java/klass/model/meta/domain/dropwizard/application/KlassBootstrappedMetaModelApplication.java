package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import io.dropwizard.setup.Environment;

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
    protected void registerLoggingFilters(@Nonnull Environment environment)
    {
        super.registerLoggingFilters(environment);

        environment.jersey().register(KlassResponseStructuredLoggingFilter.class);
    }

    @Override
    protected void registerJacksonModules(@Nonnull Environment environment)
    {
        super.registerJacksonModules(environment);

        environment.getObjectMapper().registerModule(new KlassMetaModelJacksonModule());
    }

    @Override
    public void run(
            @Nonnull KlassBootstrappedMetaModelConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);

        KlassFactory klassFactory = configuration.getKlassFactory();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().createDomainModel();

        environment.jersey().register(new JsonViewDynamicFeature(domainModel));
    }
}
