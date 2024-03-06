package cool.klass.dropwizard.bundle.bootstrap.writer;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import cool.klass.model.meta.domain.api.DomainModel;
import com.liftwizard.dropwizard.bundle.prioritized.PrioritizedBundle;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class BootstrapWriterBundle
        implements PrioritizedBundle<AbstractKlassConfiguration>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapWriterBundle.class);

    @Override
    public int getPriority()
    {
        return -2;
    }

    @Override
    public void runWithMdc(@Nonnull AbstractKlassConfiguration configuration, @Nonnull Environment environment)
    {
        boolean enabled = configuration.getBootstrapFactory().isEnabled();
        if (!enabled)
        {
            LOGGER.info("{} disabled.", BootstrapWriterBundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", BootstrapWriterBundle.class.getSimpleName());

        KlassFactory klassFactory = configuration.getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().createDataStore();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().createDomainModel();

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(domainModel, dataStore);
        klassBootstrapWriter.bootstrapMetaModel();

        LOGGER.info("Completing {}.", BootstrapWriterBundle.class.getSimpleName());
    }
}
