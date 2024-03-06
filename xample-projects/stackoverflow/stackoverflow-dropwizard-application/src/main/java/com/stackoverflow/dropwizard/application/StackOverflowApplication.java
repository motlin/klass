package com.stackoverflow.dropwizard.application;

import java.time.Clock;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import com.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;
import com.stackoverflow.graphql.runtime.wiring.StackOverflowRuntimeWiringBuilder;
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

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new StackOverflowRuntimeWiringBuilder()));
    }

    @Override
    public void run(
            @Nonnull StackOverflowConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);

        DataStore dataStore = configuration.getKlassFactory().getDataStoreFactory().createDataStore();
        Clock     clock     = configuration.getKlassFactory().getClockFactory().createClock();

        environment.jersey().register(new QuestionResourceManual(dataStore, clock));
    }
}
