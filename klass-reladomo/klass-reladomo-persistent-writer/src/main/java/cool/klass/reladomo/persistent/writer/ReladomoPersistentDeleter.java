package cool.klass.reladomo.persistent.writer;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;

public class ReladomoPersistentDeleter
{
    private final DataStore dataStore;

    public ReladomoPersistentDeleter(DataStore dataStore)
    {
        this.dataStore = dataStore;
    }

    public void deleteOrTerminate(Klass klass, @Nonnull Object persistentInstance)
    {
        this.dataStore.deleteOrTerminate(persistentInstance);
    }
}
