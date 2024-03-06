package com.stackoverflow.dropwizard.application;

import cool.klass.dropwizard.bundle.httplogging.HttpLoggingBundle;
import cool.klass.dropwizard.bundle.objectmapper.ObjectMapperBundle;
import cool.klass.dropwizard.bundle.reladomo.ReladomoBundle;
import com.stackoverflow.dropwizard.command.StackOverflowTestDataGeneratorCommand;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class StackOverflowApplication extends AbstractStackOverflowApplication
{
    public static void main(String[] args) throws Exception
    {
        new StackOverflowApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        bootstrap.addCommand(new StackOverflowTestDataGeneratorCommand());

        bootstrap.addBundle(new HttpLoggingBundle());
        bootstrap.addBundle(new ObjectMapperBundle());
        bootstrap.addBundle(new ReladomoBundle());
    }

    @Override
    public void run(
            StackOverflowConfiguration configuration,
            Environment environment)
    {
        super.run(configuration, environment);
    }
}
