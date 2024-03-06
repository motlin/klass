package cool.klass.dropwizard.bundle.objectmapper;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.jackson.ObjectMapperConfig;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

@AutoService(Bundle.class)
public class ObjectMapperBundle implements Bundle
{
    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull Environment environment)
    {
        ObjectMapperConfig.configure(environment.getObjectMapper());
    }
}
