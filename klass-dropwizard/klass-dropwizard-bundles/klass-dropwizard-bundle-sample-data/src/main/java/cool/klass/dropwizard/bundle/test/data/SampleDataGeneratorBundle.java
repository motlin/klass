package cool.klass.dropwizard.bundle.test.data;

import java.time.Instant;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactoryProvider;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactoryProvider;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactory;
import cool.klass.dropwizard.configuration.sample.data.SampleDataFactoryProvider;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.reladomo.sample.data.SampleDataGenerator;
import com.liftwizard.dropwizard.bundle.prioritized.PrioritizedBundle;
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

        SampleDataFactory     sampleDataFactory = configuration.getSampleDataFactory();
        Instant               dataInstant       = sampleDataFactory.getDataInstant();
        ImmutableList<String> skippedPackages   = sampleDataFactory.getSkippedPackages();

        DomainModel domainModel = getDomainModel(configuration);
        DataStore   dataStore   = getDataStore(configuration);

        SampleDataGenerator sampleDataGenerator = new SampleDataGenerator(
                domainModel,
                dataStore,
                dataInstant,
                skippedPackages);
        sampleDataGenerator.generate();

        LOGGER.info("Completing {}.", SampleDataGeneratorBundle.class.getSimpleName());
    }

    @Nonnull
    private static DomainModel getDomainModel(@Nonnull Object configuration)
    {
        if (!(configuration instanceof DomainModelFactoryProvider))
        {
            throw new IllegalStateException(configuration.getClass().getCanonicalName());
        }

        DomainModelFactoryProvider domainModelFactoryProvider = (DomainModelFactoryProvider) configuration;
        return domainModelFactoryProvider.getDomainModelFactory().createDomainModel();
    }

    private static DataStore getDataStore(@Nonnull Object configuration)
    {
        if (!(configuration instanceof DataStoreFactoryProvider))
        {
            throw new IllegalStateException(configuration.getClass().getCanonicalName());
        }

        DataStoreFactoryProvider dataStoreFactoryProvider = (DataStoreFactoryProvider) configuration;
        return dataStoreFactoryProvider.getDataStoreFactory().createDataStore();
    }
}
