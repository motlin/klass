package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class KlassBootstrappedMetaModelApplication extends AbstractKlassBootstrappedMetaModelApplication
{
    public static void main(String[] args) throws Exception
    {
        new KlassBootstrappedMetaModelApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<KlassBootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
    }

    @Override
    public void run(
            @Nonnull KlassBootstrappedMetaModelConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);
    }
}
