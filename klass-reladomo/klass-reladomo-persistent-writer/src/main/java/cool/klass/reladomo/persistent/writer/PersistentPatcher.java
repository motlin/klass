package cool.klass.reladomo.persistent.writer;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;

public class PersistentPatcher extends PersistentSynchronizer
{
    public PersistentPatcher(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore)
    {
        this(mutationContext, dataStore, false);
    }

    public PersistentPatcher(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore,
            boolean inTransaction)
    {
        super(mutationContext, dataStore, inTransaction);
    }

    @Override
    protected boolean shouldWriteKey()
    {
        return false;
    }

    @Override
    protected boolean shouldWriteId()
    {
        return true;
    }

    @Override
    protected void synchronizeCreatedDataTypeProperties(Klass klass, Object persistentInstance)
    {
        // Deliberately empty for update operation
    }

    @Override
    protected void handleVersion(
            AssociationEnd associationEnd,
            Object persistentInstance)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".handleVersion() not implemented yet");
    }

    @Override
    protected boolean handleToOneOutsideProjection(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstance)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".handleToOneOutsideProjection() not implemented yet");
    }

    @Nonnull
    @Override
    protected PersistentSynchronizer determineNextMode(OperationMode nextMode)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".determineNextMode() not implemented yet");
    }
}
