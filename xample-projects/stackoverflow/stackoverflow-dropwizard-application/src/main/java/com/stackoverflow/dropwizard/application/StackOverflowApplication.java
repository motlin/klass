package com.stackoverflow.dropwizard.application;

import java.time.Clock;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import com.stackoverflow.graphql.runtime.wiring.StackOverflowRuntimeWiringBuilder;
import com.stackoverflow.service.resource.QuestionResourceManual;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;

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

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new StackOverflowRuntimeWiringBuilder()));
    }

    @Override
    public void run(
            @Nonnull StackOverflowConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);

        KlassFactory klassFactory = configuration.getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().createDataStore();
        Clock        clock        = klassFactory.getClockFactory().createClock();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().createDomainModel();

        environment.jersey().register(new QuestionResourceManual(domainModel, dataStore, clock));
    }
}
