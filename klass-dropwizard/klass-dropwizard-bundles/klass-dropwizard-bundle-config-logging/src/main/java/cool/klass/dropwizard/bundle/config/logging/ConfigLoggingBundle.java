package cool.klass.dropwizard.bundle.config.logging;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.config.logging.ConfigLoggingFactoryProvider;
import cool.klass.dropwizard.configuration.enabled.EnabledFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class ConfigLoggingBundle implements PrioritizedBundle<ConfigLoggingFactoryProvider>
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
            @Nonnull ConfigLoggingFactoryProvider configuration,
            @Nonnull Environment environment) throws JsonProcessingException, ReflectiveOperationException
    {
        EnabledFactory configLoggingFactory = configuration.getConfigLoggingFactory();
        if (!configLoggingFactory.isEnabled())
        {
            LOGGER.info("{} disabled.", ConfigLoggingBundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", ConfigLoggingBundle.class.getSimpleName());

        ConfigLoggingBundle.logConfiguration(configuration, environment.getObjectMapper());

        LOGGER.info("Completing {}.", ConfigLoggingBundle.class.getSimpleName());
    }

    private static void logConfiguration(
            @Nonnull ConfigLoggingFactoryProvider configuration,
            ObjectMapper objectMapper) throws JsonProcessingException, ReflectiveOperationException
    {
        String configurationString = objectMapper.writeValueAsString(configuration);
        LOGGER.debug("Inferred Dropwizard configuration:\n{}", configurationString);

        ConfigLoggingFactoryProvider defaultConfiguration       = configuration.getClass().getConstructor().newInstance();
        String                     defaultConfigurationString = objectMapper.writeValueAsString(defaultConfiguration);
        LOGGER.debug("Default Dropwizard configuration:\n{}", defaultConfigurationString);
    }
}
