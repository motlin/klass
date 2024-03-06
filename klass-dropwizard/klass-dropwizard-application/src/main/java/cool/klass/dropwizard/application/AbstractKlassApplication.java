package cool.klass.dropwizard.application;

import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import ch.qos.logback.classic.Level;
import cool.klass.dropwizard.bundle.api.DataBundle;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.dropwizard.configuration.clock.ClockFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.loader.DomainModelLoader;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Application;
import io.dropwizard.ConfiguredBundle;
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

    protected DomainModel domainModel;
    protected Clock       clock;

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

    public final DomainModel getDomainModel()
    {
        return Objects.requireNonNull(this.domainModel);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<T> bootstrap)
    {
        super.initialize(bootstrap);

        Config config = ConfigFactory.load();

        this.logConfig(config.root().withOnlyKey("klass").toConfig());

        this.domainModel = this.loadDomainModel(config);

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

        for (ConfiguredBundle bundle : prioritizedBundles)
        {
            if (bundle instanceof DataBundle)
            {
                DataBundle dataBundle = (DataBundle) bundle;
                dataBundle.initialize(this.domainModel);
            }

            bootstrap.addBundle(bundle);
        }
    }

    private DomainModel loadDomainModel(@Nonnull Config config)
    {
        List<String>      klassSourcePackages = config.getStringList("klass.domain.sourcePackages");
        DomainModelLoader domainModelLoader   = new DomainModelLoader(Lists.immutable.withAll(klassSourcePackages));
        return domainModelLoader.load();
    }

    @Override
    public void run(@Nonnull T configuration, Environment environment) throws Exception
    {
        this.initializeClock(configuration);
    }

    protected void initializeClock(@Nonnull T configuration)
    {
        KlassFactory klassFactory = configuration.getKlassFactory();
        ClockFactory clockFactory = klassFactory.getClockFactory();
        this.clock = clockFactory.getClock();
    }
}
