package cool.klass.reladomo.persistent.writer;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;

public class ReladomoPersistentDeleter
{
    private final DataStore dataStore;

    public ReladomoPersistentDeleter(DataStore dataStore)
    {
        this.dataStore = dataStore;
    }

    public void deleteOrTerminate(Object persistentChildInstance, ProjectionParent projectionParent)
    {
        this.dataStore.deleteOrTerminate(persistentChildInstance);
    }
}
