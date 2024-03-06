package cool.klass.dropwizard.bundle.bootstrap.writer;

import java.util.Objects;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.api.DataBundle;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
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
        implements DataBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapWriterBundle.class);

    private DomainModel domainModel;

    @Override
    public int getPriority()
    {
        return -2;
    }

    @Override
    public void initialize(DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(AbstractKlassConfiguration configuration, Environment environment)
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

        DataStore dataStore = configuration
                .getKlassFactory()
                .getDataStoreFactory()
                .getDataStore();

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(this.domainModel, dataStore);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
