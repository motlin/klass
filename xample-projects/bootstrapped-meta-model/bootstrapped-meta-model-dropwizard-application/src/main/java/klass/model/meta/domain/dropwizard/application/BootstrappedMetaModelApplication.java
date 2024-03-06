package klass.model.meta.domain.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class BootstrappedMetaModelApplication extends AbstractBootstrappedMetaModelApplication
{
    public static void main(String[] args) throws Exception
    {
        new BootstrappedMetaModelApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<BootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        // TODO: application initialization
    }

    @Override
    public void run(
            BootstrappedMetaModelConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);
    }
}
