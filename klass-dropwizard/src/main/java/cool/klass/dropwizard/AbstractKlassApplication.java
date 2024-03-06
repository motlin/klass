package cool.klass.dropwizard;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.dropwizard.bundle.api.KlassBundle;
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
    public void initialize(Bootstrap<T> bootstrap)
    {
        super.initialize(bootstrap);

        Config klassConfig = ConfigFactory.load().getConfig("klass");

        this.logConfig(klassConfig);

        this.domainModel = this.loadDomainModel(klassConfig);

        // TODO: Choose data store from configuration
        this.dataStore = new ReladomoDataStore();

        this.initializeDynamicBundles(bootstrap);
        this.initializeDynamicKlassBundles(bootstrap);
    }

    private void logConfig(Config klassConfig)
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
        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        ImmutableList<Bundle> bundles             = Lists.immutable.withAll(bundleServiceLoader);
        LOGGER.info("Found Bundles: {}", bundles.makeString());
        for (Bundle bundle : bundles)
        {
            if (bundle instanceof KlassBundle)
            {
                throw new AssertionError();
            }

            bootstrap.addBundle(bundle);
        }
    }

    private void initializeDynamicKlassBundles(@Nonnull Bootstrap<T> bootstrap)
    {
        ServiceLoader<KlassBundle> klassBundleServiceLoader = ServiceLoader.load(KlassBundle.class);
        ImmutableList<KlassBundle> klassBundles             = Lists.immutable.withAll(klassBundleServiceLoader);
        LOGGER.info("Found KlassBundles: {}", klassBundles.makeString());
        for (KlassBundle klassBundle : klassBundleServiceLoader)
        {
            klassBundle.initialize(this.domainModel, this.dataStore);
            bootstrap.addBundle(klassBundle);
        }
    }

    private DomainModel loadDomainModel(Config klassConfig)
    {
        List<String>      klassSourcePackages = klassConfig.getStringList("domain.sourcePackages");
        DomainModelLoader domainModelLoader   = new DomainModelLoader(Lists.immutable.withAll(klassSourcePackages));
        return domainModelLoader.load();
    }

    @Override
    public void run(T configuration, Environment environment) throws Exception
    {
        Config config      = ConfigFactory.load();
        Config klassConfig = config.getConfig("klass");
        ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                .setJson(false)
                .setOriginComments(false);
        String render = klassConfig.root().render(configRenderOptions);
        LOGGER.info("Klass HOCON configuration:\n{}", render);
    }
}
