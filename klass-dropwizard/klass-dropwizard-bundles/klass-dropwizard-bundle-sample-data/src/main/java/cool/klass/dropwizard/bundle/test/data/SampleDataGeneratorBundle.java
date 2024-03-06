package cool.klass.dropwizard.bundle.test.data;

import java.time.Instant;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactory;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactoryProvider;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.reladomo.sample.data.SampleDataGenerator;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class SampleDataGeneratorBundle
        implements PrioritizedBundle<SampleDataFactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleDataGeneratorBundle.class);

    @Override
    public int getPriority()
    {
        return -1;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull SampleDataFactoryProvider configuration, Environment environment)
    {
        boolean enabled = configuration.getSampleDataFactory().isEnabled();
        if (!enabled)
        {
            LOGGER.info("{} disabled.", SampleDataGeneratorBundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", SampleDataGeneratorBundle.class.getSimpleName());

        DomainModel           domainModel       = configuration.getDomainModelFactory().createDomainModel();
        DataStore             dataStore         = configuration.getDataStoreFactory().createDataStore();
        SampleDataFactory     sampleDataFactory = configuration.getSampleDataFactory();
        Instant               dataInstant       = sampleDataFactory.getDataInstant();
        ImmutableList<String> skippedPackages   = sampleDataFactory.getSkippedPackages();

        SampleDataGenerator sampleDataGenerator = new SampleDataGenerator(
                domainModel,
                dataStore,
                dataInstant,
                skippedPackages);
        sampleDataGenerator.generate();

        LOGGER.info("Completing {}.", SampleDataGeneratorBundle.class.getSimpleName());
    }
}
