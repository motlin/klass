package cool.klass.dropwizard.bundle.reladomo;

import cool.klass.reladomo.configuration.ReladomoConfig;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ReladomoBundle implements Bundle
{
    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(Environment environment)
    {
        ReladomoConfig.configure();
    }
}
