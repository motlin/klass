package com.stackoverflow.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import com.stackoverflow.dropwizard.command.StackOverflowDemoCommand;
import com.stackoverflow.service.resource.QuestionResourceManual;
import io.dropwizard.Bundle;
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

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        // TODO: Move up to generated code
        bootstrap.addCommand(new StackOverflowDemoCommand(this));

        // TODO: application initialization
    }

    @Override
    public void run(
            StackOverflowConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);

        environment.jersey().register(new QuestionResourceManual(new ReladomoDataStore()));

        // TODO: Move up to generated superclass
        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(this.domainModel);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
