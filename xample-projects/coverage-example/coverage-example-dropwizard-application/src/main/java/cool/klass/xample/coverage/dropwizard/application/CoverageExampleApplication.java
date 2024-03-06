package cool.klass.xample.coverage.dropwizard.application;

import javax.annotation.Nonnull;

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

        // TODO: application initialization
    }

    @Override
    public void run(
            @Nonnull CoverageExampleConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);
    }
}
