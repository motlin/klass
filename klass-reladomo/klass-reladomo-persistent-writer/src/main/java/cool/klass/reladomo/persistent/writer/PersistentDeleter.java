package cool.klass.reladomo.persistent.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;

public class PersistentDeleter
{
    @Nonnull
    private final MutationContext mutationContext;
    private final DataStore       dataStore;

    public PersistentDeleter(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore)
    {
        this.mutationContext = Objects.requireNonNull(mutationContext);
        this.dataStore       = Objects.requireNonNull(dataStore);
    }

    public void deleteOrTerminate(Klass klass, @Nonnull Object persistentInstance)
    {
        this.dataStore.deleteOrTerminate(persistentInstance);
    }
}
