package cool.klass.xample.coverage.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.bundle.test.data.TestDataGeneratorBundle;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CoverageExampleApplication extends AbstractCoverageExampleApplication
{
    public static void main(String[] args) throws Exception
    {
        new CoverageExampleApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        bootstrap.addBundle(new TestDataGeneratorBundle(this.domainModel, this.dataStore));

        // TODO: application initialization
    }

    @Override
    public void run(
            CoverageExampleConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);

        // TODO: Move up to generated superclass
        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(this.domainModel);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
