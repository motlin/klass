package com.stackoverflow.dropwizard.application;

import java.time.Clock;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import com.stackoverflow.service.resource.QuestionResourceManual;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class StackOverflowApplication
        extends AbstractStackOverflowApplication
{
    public static void main(String[] args) throws Exception
    {
        new StackOverflowApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        // TODO: Implement TypeResolvers for Interfaces
        // https://stackoverflow.com/questions/54251935/graphql-no-resolver-definied-for-interface-union-java
        // bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new StackOverflowRuntimeWiringBuilder()));
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
            @Nonnull StackOverflowConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        KlassFactory klassFactory = configuration.getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().createDataStore();
        Clock        clock        = klassFactory.getClockFactory().createClock();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().createDomainModel();

        environment.jersey().register(new JsonViewDynamicFeature(domainModel));

        environment.jersey().register(new QuestionResourceManual(domainModel, dataStore, clock));

        super.run(configuration, environment);
    }
}
