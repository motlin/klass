package com.stackoverflow.dropwizard.application;

import java.time.Clock;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import com.stackoverflow.service.resource.QuestionResourceManual;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class StackOverflowApplication extends AbstractStackOverflowApplication
{
    public static void main(String[] args) throws Exception
    {
        new StackOverflowApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
        bootstrap.addBundle(new StackOverflowGraphQLBundle());
    }

    @Override
    public void run(
            @Nonnull StackOverflowConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);

        DataStore dataStore = configuration.getKlassFactory().getDataStoreFactory().getDataStore();
        Clock     clock     = configuration.getKlassFactory().getClockFactory().getClock();

        environment.jersey().register(new QuestionResourceManual(dataStore, clock));
    }
}
