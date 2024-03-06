package cool.klass.dropwizard.bundle.httplogging;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.logging.LoggingFeature.Verbosity;
import org.slf4j.LoggerFactory;

public class HttpLoggingBundle implements Bundle
{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpLoggingBundle.class);

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull Environment environment)
    {
        Config config              = ConfigFactory.load();
        Config jerseyLoggingConfig = config.getConfig("jersey.logging");
        String levelString         = jerseyLoggingConfig.getString("level");
        String verbosityString     = jerseyLoggingConfig.getString("verbosity");
        int    maxEntitySize       = jerseyLoggingConfig.getInt("maxEntitySize");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = jerseyLoggingConfig.root().render(configRenderOptions);
            LOGGER.debug("Jersey logging configuration:\n{}", render);
        }

        Logger         logger         = Logger.getLogger(HttpLoggingBundle.class.getName());
        Level          level          = Level.parse(levelString);
        Verbosity      verbosity      = Verbosity.valueOf(verbosityString);
        LoggingFeature loggingFeature = new LoggingFeature(logger, level, verbosity, maxEntitySize);
        environment.jersey().register(loggingFeature);
    }
}
