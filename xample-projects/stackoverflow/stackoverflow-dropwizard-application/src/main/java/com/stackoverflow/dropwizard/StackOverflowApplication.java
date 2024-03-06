package com.stackoverflow.dropwizard;

import javax.annotation.Nonnull;
import javax.ws.rs.GET;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class StackOverflowApplication extends Application<StackOverflowConfiguration>
{
    public static void main(String[] args) throws Exception
    {
        new StackOverflowApplication().run(args);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "StackOverflow";
    }

    @Override
    public void initialize(Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        // TODO: application initialization
    }

    @GET
    @Override
    public void run(
            StackOverflowConfiguration configuration,
            Environment environment)
    {
    }
}
