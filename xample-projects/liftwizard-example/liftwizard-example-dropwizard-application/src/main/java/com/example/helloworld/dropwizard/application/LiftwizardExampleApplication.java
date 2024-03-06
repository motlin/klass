package com.example.helloworld.dropwizard.application;

import javax.annotation.Nonnull;

import com.example.helloworld.graphql.runtime.wiring.LiftwizardExampleRuntimeWiringBuilder;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;

public class LiftwizardExampleApplication
        extends AbstractLiftwizardExampleApplication
{
    public static void main(String[] args) throws Exception
    {
        new LiftwizardExampleApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<LiftwizardExampleConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<LiftwizardExampleConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new LiftwizardExampleRuntimeWiringBuilder()));
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
            @Nonnull LiftwizardExampleConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);
    }
}
