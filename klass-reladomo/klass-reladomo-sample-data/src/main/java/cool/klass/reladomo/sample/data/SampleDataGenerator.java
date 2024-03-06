package cool.klass.reladomo.sample.data;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.InheritanceType;
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
            ImmutableList<Klass> classesWithTables = this.domainModel.getKlasses().select(this::needsTable);
            classesWithTables.each(this::generate);
        });
    }

    private boolean needsTable(@Nonnull Klass klass)
    {
        return klass.getInheritanceType() == InheritanceType.NONE
                || klass.getInheritanceType() == InheritanceType.TABLE_PER_CLASS;
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
