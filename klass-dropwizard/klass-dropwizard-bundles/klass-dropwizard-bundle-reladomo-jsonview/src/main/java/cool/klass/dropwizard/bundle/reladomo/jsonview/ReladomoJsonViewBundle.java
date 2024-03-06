package cool.klass.dropwizard.bundle.reladomo.jsonview;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.auto.service.AutoService;
import com.gs.fw.common.mithra.MithraObject;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.reladomo.ReladomoFactoryProvider;
import cool.klass.serialization.jackson.jsonview.reladomo.ReladomoJsonViewSerializer;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Break up ReladomoFactoryProvider
@AutoService(PrioritizedBundle.class)
public class ReladomoJsonViewBundle implements PrioritizedBundle<ReladomoFactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoJsonViewBundle.class);

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
    public void run(@Nonnull ReladomoFactoryProvider configuration, @Nonnull Environment environment)
    {
        LOGGER.info("Running {}.", ReladomoJsonViewBundle.class.getSimpleName());

        DataStore                    dataStore  = configuration.getDataStoreFactory().createDataStore();
        JsonSerializer<MithraObject> serializer = new ReladomoJsonViewSerializer(dataStore);

        SimpleModule module = new SimpleModule();
        module.addSerializer(MithraObject.class, serializer);
        environment.getObjectMapper().registerModule(module);

        LOGGER.info("Completing {}.", ReladomoJsonViewBundle.class.getSimpleName());
    }
}
