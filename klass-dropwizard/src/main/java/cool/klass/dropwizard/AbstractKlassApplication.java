package cool.klass.dropwizard;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import ch.qos.logback.classic.Level;
import cool.klass.data.store.DataStore;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.dropwizard.bundle.api.DataBundle;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.loader.DomainModelLoader;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKlassApplication<T extends Configuration> extends Application<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKlassApplication.class);

    protected final String name;

    protected DomainModel domainModel;
    protected DataStore   dataStore;

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

    public final DataStore getDataStore()
    {
        return Objects.requireNonNull(this.dataStore);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<T> bootstrap)
    {
        super.initialize(bootstrap);

        Config klassConfig = ConfigFactory.load().getConfig("klass");

        this.logConfig(klassConfig);

        this.domainModel = this.loadDomainModel(klassConfig);

        // TODO: Choose data store from configuration
        this.dataStore = new ReladomoDataStore();

        this.initializeDynamicBundles(bootstrap);
    }

    private void logConfig(@Nonnull Config klassConfig)
    {
        if (LOGGER.isInfoEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = klassConfig.root().render(configRenderOptions);
            LOGGER.info("Klass configuration:\n{}", render);
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

        for (Bundle bundle : prioritizedBundles)
        {
            if (bundle instanceof DataBundle)
            {
                DataBundle dataBundle = (DataBundle) bundle;
                dataBundle.initialize(this.domainModel, this.dataStore);
            }

            bootstrap.addBundle(bundle);
        }
    }

    private DomainModel loadDomainModel(@Nonnull Config klassConfig)
    {
        List<String>      klassSourcePackages = klassConfig.getStringList("domain.sourcePackages");
        DomainModelLoader domainModelLoader   = new DomainModelLoader(Lists.immutable.withAll(klassSourcePackages));
        return domainModelLoader.load();
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception
    {
    }
}
