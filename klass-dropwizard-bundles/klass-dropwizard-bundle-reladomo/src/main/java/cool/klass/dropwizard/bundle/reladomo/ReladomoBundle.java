package cool.klass.dropwizard.bundle.reladomo;

import java.util.Objects;

import com.google.auto.service.AutoService;
import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.api.DataBundle;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.reladomo.configuration.ReladomoConfig;
import cool.klass.serializer.json.ReladomoJsonSerializer;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

@AutoService(PrioritizedBundle.class)
public class ReladomoBundle implements DataBundle
{
    private DomainModel domainModel;
    private DataStore   dataStore;

    @Override
    public int getPriority()
    {
        return -3;
    }

    @Override
    public void initialize(DomainModel domainModel, DataStore dataStore)
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
        ReladomoConfig.configure(
                environment.getObjectMapper(),
                new ReladomoJsonSerializer(this.domainModel, this.dataStore));

        environment.metrics().gauge(
                "Reladomo database retrieve count",
                () -> () -> MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount());
        environment.metrics().gauge(
                "Reladomo remote retrieve count",
                () -> () -> MithraManagerProvider.getMithraManager().getRemoteRetrieveCount());
    }
}
