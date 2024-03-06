package cool.klass.dropwizard.bundle.objectmapper;

import cool.klass.jackson.ObjectMapperConfig;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ObjectMapperBundle implements Bundle
{
    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(Environment environment)
    {
        ObjectMapperConfig.configure(environment.getObjectMapper());
    }
}
