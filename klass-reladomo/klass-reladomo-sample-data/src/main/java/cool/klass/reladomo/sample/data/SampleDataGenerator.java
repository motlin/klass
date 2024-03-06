package cool.klass.reladomo.sample.data;

import java.time.Instant;
import java.util.Objects;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;

public class SampleDataGenerator
{
    private final DomainModel domainModel;
    private final DataStore   dataStore;

    private final Instant               systemTime;
    private final ImmutableList<String> skippedPackages;

    private final KlassRequiredDataGenerator requiredDataGenerator;
    private final KlassOptionalDataGenerator optionalDataGenerator;

    public SampleDataGenerator(
            DomainModel domainModel,
            DataStore dataStore,
            Instant systemTime,
            ImmutableList<String> skippedPackages)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore = Objects.requireNonNull(dataStore);
        this.systemTime = Objects.requireNonNull(systemTime);
        this.skippedPackages = Objects.requireNonNull(skippedPackages);

        this.requiredDataGenerator = new KlassRequiredDataGenerator(this.dataStore);
        this.optionalDataGenerator = new KlassOptionalDataGenerator(this.dataStore);
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
        if (this.skippedPackages.contains(klass.getPackageName()))
        {
            return;
        }
        this.requiredDataGenerator.generateIfRequired(klass);
        this.optionalDataGenerator.generateIfRequired(klass);
    }
}
