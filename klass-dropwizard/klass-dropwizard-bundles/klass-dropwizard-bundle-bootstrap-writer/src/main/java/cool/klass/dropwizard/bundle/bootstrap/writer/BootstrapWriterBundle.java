package cool.klass.dropwizard.bundle.bootstrap.writer;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import cool.klass.model.meta.domain.api.DomainModel;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class BootstrapWriterBundle
        implements PrioritizedBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapWriterBundle.class);

    @Override
    public int getPriority()
    {
        return -2;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull AbstractKlassConfiguration configuration, Environment environment)
    {
        Config config          = ConfigFactory.load();
        Config bootstrapConfig = config.getConfig("klass.data.reladomo.bootstrap");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = bootstrapConfig.root().render(configRenderOptions);
            LOGGER.debug("Bootstrap Writer Bundle configuration:\n{}", render);
        }

        boolean enabled = bootstrapConfig.getBoolean("enabled");
        if (!enabled)
        {
            return;
        }

        KlassFactory klassFactory = configuration.getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().getDataStore();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().getDomainModel();

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(domainModel, dataStore);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
