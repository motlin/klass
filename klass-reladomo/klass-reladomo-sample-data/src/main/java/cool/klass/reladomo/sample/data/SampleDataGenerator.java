package cool.klass.reladomo.sample.data;

import java.time.Instant;
import java.util.Objects;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;

public class SampleDataGenerator
{
    private final DomainModel domainModel;
    private final DataStore   dataStore;

    private final Instant systemTime;

    public SampleDataGenerator(DomainModel domainModel, DataStore dataStore, Instant systemTime)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore = Objects.requireNonNull(dataStore);
        this.systemTime = systemTime;
    }

    public void generate()
    {
        this.dataStore.runInTransaction(transaction ->
        {
            transaction.setSystemTime(this.systemTime.toEpochMilli());
            this.domainModel.getKlasses().reject(Klass::isAbstract).each(this::generate);
        });
    }

    private void generate(Klass klass)
    {
        new KlassRequiredDataGenerator(this.dataStore, klass).generateIfRequired();
        new KlassOptionalDataGenerator(this.dataStore, klass).generateIfRequired();
    }
}
