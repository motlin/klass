package cool.klass.dropwizard.bundle.reladomo;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.reladomo.configuration.ReladomoConfig;
import cool.klass.serializer.json.ReladomoJsonSerializer;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

@AutoService(PrioritizedBundle.class)
public class ReladomoBundle implements PrioritizedBundle
{
    @Override
    public int getPriority()
    {
        return -3;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull AbstractKlassConfiguration configuration, @Nonnull Environment environment)
    {
        KlassFactory klassFactory = configuration.getKlassFactory();
        DataStore dataStore = klassFactory.getDataStoreFactory().getDataStore();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().getDomainModel();

        ReladomoJsonSerializer reladomoJsonSerializer = new ReladomoJsonSerializer(domainModel, dataStore);
        ReladomoConfig.configure(environment.getObjectMapper(), reladomoJsonSerializer);

        environment.metrics().gauge(
                "Reladomo database retrieve count",
                () -> () -> MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount());
        environment.metrics().gauge(
                "Reladomo remote retrieve count",
                () -> () -> MithraManagerProvider.getMithraManager().getRemoteRetrieveCount());
    }
}
