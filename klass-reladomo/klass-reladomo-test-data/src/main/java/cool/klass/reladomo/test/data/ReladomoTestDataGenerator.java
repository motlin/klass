package cool.klass.reladomo.test.data;

import java.time.Instant;
import java.util.Objects;

import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;

public class ReladomoTestDataGenerator
{
    private final DomainModel domainModel;
    private final DataStore   dataStore;

    private final Instant systemTime;

    public ReladomoTestDataGenerator(DomainModel domainModel, DataStore dataStore, Instant systemTime)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore = Objects.requireNonNull(dataStore);
        this.systemTime = systemTime;
    }

    public void generate()
    {
        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            tx.setProcessingStartTime(this.systemTime.toEpochMilli());
            this.domainModel.getKlasses().each(this::generate);
            return null;
        });
    }

    private void generate(Klass klass)
    {
        new ReladomoKlassRequiredDataGenerator(this.dataStore, klass).generateIfRequired();
        new ReladomoKlassOptionalDataGenerator(this.dataStore, klass).generateIfRequired();
    }
}
