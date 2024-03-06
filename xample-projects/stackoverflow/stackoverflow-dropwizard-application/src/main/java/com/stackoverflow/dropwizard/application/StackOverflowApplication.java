package com.stackoverflow.dropwizard.application;

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
    public void initialize(Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
    }

    @Override
    public void run(
            StackOverflowConfiguration configuration,
            Environment environment)
    {
        super.run(configuration, environment);

        environment.jersey().register(new QuestionResourceManual(this.domainModel));
    }
}
