package cool.klass.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import com.liftwizard.dropwizard.application.AbstractLiftwizardApplication;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.setup.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKlassApplication<T extends AbstractKlassConfiguration>
        extends AbstractLiftwizardApplication<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKlassApplication.class);

    protected AbstractKlassApplication(String name)
    {
        super(name);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<T> bootstrap)
    {
        super.initialize(bootstrap);

        Config config = ConfigFactory.load();
        this.logConfig(config.root().withOnlyKey("klass").toConfig());
    }

    protected void logConfig(@Nonnull Config config)
    {
        if (LOGGER.isInfoEnabled())
        {
            // TODO: Make configurable
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false)
                    .setComments(true)
                    .setFormatted(true);
            String render = config.root().render(configRenderOptions);
            LOGGER.info("HOCON configuration:\n{}", render);
        }
    }
}
