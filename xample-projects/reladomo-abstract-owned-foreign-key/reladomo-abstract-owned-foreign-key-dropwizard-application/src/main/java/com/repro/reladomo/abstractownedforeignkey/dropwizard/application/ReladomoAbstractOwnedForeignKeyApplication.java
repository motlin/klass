package com.repro.reladomo.abstractownedforeignkey.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.bundle.bootstrap.writer.BootstrapWriterBundle;
import cool.klass.dropwizard.bundle.test.data.SampleDataGeneratorBundle;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReladomoAbstractOwnedForeignKeyApplication extends AbstractReladomoAbstractOwnedForeignKeyApplication
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoAbstractOwnedForeignKeyApplication.class);

    public static void main(String[] args) throws Exception
    {
        new ReladomoAbstractOwnedForeignKeyApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<ReladomoAbstractOwnedForeignKeyConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        // TODO: Move up?
        bootstrap.addBundle(new SampleDataGeneratorBundle(this.domainModel, this.dataStore));
        bootstrap.addBundle(new BootstrapWriterBundle(this.domainModel, this.dataStore));

        // TODO: application initialization
    }

    @Override
    public void run(
            ReladomoAbstractOwnedForeignKeyConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);
    }
}
