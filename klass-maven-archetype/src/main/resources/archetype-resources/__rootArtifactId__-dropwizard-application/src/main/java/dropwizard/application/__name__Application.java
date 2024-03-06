package ${package}.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import ${package}.graphql.runtime.wiring.${name}RuntimeWiringBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;

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
    protected void initializeBundles(@Nonnull Bootstrap<${name}Configuration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new ${name}RuntimeWiringBuilder()));
    }

    @Override
    protected void registerLoggingFilters(@Nonnull Environment environment)
    {
        super.registerLoggingFilters(environment);

        environment.jersey().register(KlassResponseStructuredLoggingFilter.class);
        environment.jersey().register(JsonViewDynamicFeature.class);
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
        DomainModel domainModel = configuration.getKlassFactory().getDomainModelFactory().createDomainModel();

        environment.jersey().register(new JsonViewDynamicFeature(domainModel));

        super.run(configuration, environment);
    }
}
