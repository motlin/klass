package cool.klass.xample.coverage.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import cool.klass.xample.coverage.graphql.runtime.wiring.CoverageExampleRuntimeWiringBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;

public class CoverageExampleApplication
        extends AbstractCoverageExampleApplication
{
    public static void main(String[] args) throws Exception
    {
        new CoverageExampleApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initialize(bootstrap);
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new CoverageExampleRuntimeWiringBuilder()));
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
            @Nonnull CoverageExampleConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        DomainModel domainModel = configuration.getKlassFactory().getDomainModelFactory().createDomainModel();

        environment.jersey().register(new JsonViewDynamicFeature(domainModel));

        super.run(configuration, environment);
    }
}
