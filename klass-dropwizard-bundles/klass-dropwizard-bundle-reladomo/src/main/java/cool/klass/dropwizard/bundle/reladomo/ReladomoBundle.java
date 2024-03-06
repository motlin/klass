package cool.klass.dropwizard.bundle.reladomo;

import com.google.auto.service.AutoService;
import cool.klass.reladomo.configuration.ReladomoConfig;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

@AutoService(Bundle.class)
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
