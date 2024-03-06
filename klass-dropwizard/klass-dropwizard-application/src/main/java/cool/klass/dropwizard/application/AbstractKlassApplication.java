package cool.klass.dropwizard.application;

import java.util.Objects;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import ch.qos.logback.classic.Level;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import com.liftwizard.dropwizard.bundle.prioritized.PrioritizedBundle;
import com.liftwizard.dropwizard.task.reladomo.clear.cache.ReladomoClearCacheTask;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKlassApplication<T extends AbstractKlassConfiguration> extends Application<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKlassApplication.class);

    protected final String name;

    protected AbstractKlassApplication(String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    protected Level bootstrapLogLevel()
    {
        return Level.DEBUG;
    }

    @Nonnull
    @Override
    public final String getName()
    {
        return this.name;
    }

    @Override
    public void initialize(@Nonnull Bootstrap<T> bootstrap)
    {
        super.initialize(bootstrap);

        Config config = ConfigFactory.load();

        this.logConfig(config.root().withOnlyKey("klass").toConfig());

        this.initializeDynamicBundles(bootstrap);
    }

    private void logConfig(@Nonnull Config config)
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

    private void initializeDynamicBundles(@Nonnull Bootstrap<T> bootstrap)
    {
        ServiceLoader<PrioritizedBundle> serviceLoader = ServiceLoader.load(PrioritizedBundle.class);
        ImmutableList<PrioritizedBundle> prioritizedBundles = Lists.immutable.withAll(serviceLoader)
                .toSortedListBy(PrioritizedBundle::getPriority)
                .toImmutable();

        if (prioritizedBundles.isEmpty())
        {
            LOGGER.warn("Didn't find any implementations of PrioritizedBundle using ServiceLoader.");
        }

        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info(
                    "Found PrioritizedBundles using ServiceLoader:\n{}",
                    prioritizedBundles
                            .collect(Object::getClass)
                            .collect(Class::getSimpleName)
                            .collect("    "::concat)
                            .makeString("\n"));
        }

        for (PrioritizedBundle bundle : prioritizedBundles)
        {
            bootstrap.addBundle(bundle);
        }
    }

    @Override
    public void run(@Nonnull T configuration, @Nonnull Environment environment) throws Exception
    {
        environment.admin().addTask(new ReladomoClearCacheTask());
    }
}
