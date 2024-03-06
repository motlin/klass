package cool.klass.dropwizard.bundle.reladomo.response;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactoryProvider;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactoryProvider;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.serialization.jackson.response.KlassResponse;
import cool.klass.serialization.jackson.response.reladomo.KlassResponseReladomoJsonSerializer;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.prioritized.PrioritizedBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class ReladomoResponseBundle
        implements PrioritizedBundle<Object>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoResponseBundle.class);

    @Override
    public int getPriority()
    {
        return -2;
    }

    @Override
    public void runWithMdc(
            @Nonnull Object configuration,
            @Nonnull Environment environment)
    {
        DomainModelFactoryProvider domainModelFactoryProvider =
                this.safeCastConfiguration(DomainModelFactoryProvider.class, configuration);

        DataStoreFactoryProvider dataStoreFactoryProvider = this.safeCastConfiguration(
                DataStoreFactoryProvider.class,
                configuration);

        LOGGER.info("Running {}.", ReladomoResponseBundle.class.getSimpleName());

        DomainModel domainModel = domainModelFactoryProvider.getDomainModelFactory().createDomainModel();
        DataStore   dataStore   = dataStoreFactoryProvider.getDataStoreFactory().createDataStore();

        JsonSerializer<KlassResponse> serializer = new KlassResponseReladomoJsonSerializer(domainModel, dataStore);

        SimpleModule module = new SimpleModule();
        module.addSerializer(KlassResponse.class, serializer);
        environment.getObjectMapper().registerModule(module);

        LOGGER.info("Completing {}.", ReladomoResponseBundle.class.getSimpleName());
    }
}
