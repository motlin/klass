package cool.klass.dropwizard.bundle.config.logging;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.EnabledFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.impl.map.mutable.MapAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class ConfigLoggingBundle implements PrioritizedBundle<AbstractKlassConfiguration>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoggingBundle.class);

    @Override
    public int getPriority()
    {
        return -5;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(
            @Nonnull AbstractKlassConfiguration configuration,
            @Nonnull Environment environment) throws JsonProcessingException
    {
        EnabledFactory configLoggingFactory = configuration.getConfigLoggingFactory();
        if (!configLoggingFactory.isEnabled())
        {
            LOGGER.info("{} disabled.", ConfigLoggingBundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", ConfigLoggingBundle.class.getSimpleName());

        this.logConfiguration(configuration, environment.getObjectMapper());

        LOGGER.info("Completing {}.", ConfigLoggingBundle.class.getSimpleName());
    }

    private void logConfiguration(
            @Nonnull AbstractKlassConfiguration configuration,
            ObjectMapper objectMapper) throws JsonProcessingException
    {
        String configurationString = objectMapper.writeValueAsString(configuration);
        LOGGER.info("Inferred Dropwizard configuration:\n{}", configurationString);

        String environmentString = MapAdapter.adapt(System.getenv())
                .keyValuesView()
                .makeString("\n");
        LOGGER.info("Environment:\n{}", environmentString);
    }
}
