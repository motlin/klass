package cool.klass.dropwizard.bundle.bootstrap.writer;

import java.util.Objects;

import cool.klass.data.store.DataStore;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import cool.klass.model.meta.domain.api.DomainModel;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootstrapWriterBundle implements Bundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapWriterBundle.class);

    private final DomainModel domainModel;
    private final DataStore   dataStore;

    public BootstrapWriterBundle(DomainModel domainModel, DataStore dataStore)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore = Objects.requireNonNull(dataStore);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(Environment environment)
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

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(this.domainModel, this.dataStore);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
