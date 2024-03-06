package cool.klass.dropwizard.bundle.objectmapper;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.object.mapper.ObjectMapperFactory;
import cool.klass.dropwizard.configuration.object.mapper.ObjectMapperFactoryProvider;
import cool.klass.serialization.jackson.config.ObjectMapperConfig;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class ObjectMapperBundle
        implements PrioritizedBundle<ObjectMapperFactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperBundle.class);

    @Override
    public int getPriority()
    {
        return -6;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(ObjectMapperFactoryProvider configuration, @Nonnull Environment environment)
    {
        ObjectMapperFactory objectMapperFactory = configuration.getObjectMapperFactory();
        if (!objectMapperFactory.isEnabled())
        {
            LOGGER.info("{} disabled.", ObjectMapperBundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", ObjectMapperBundle.class.getSimpleName());

        ObjectMapperConfig.configure(
                environment.getObjectMapper(),
                objectMapperFactory.isPrettyPrint(),
                objectMapperFactory.getSerializationInclusion());

        LOGGER.info("Completing {}.", ObjectMapperBundle.class.getSimpleName());
    }
}
