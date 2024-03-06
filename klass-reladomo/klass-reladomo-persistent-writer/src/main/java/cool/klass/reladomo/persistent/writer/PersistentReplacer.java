package cool.klass.reladomo.persistent.writer;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class PersistentReplacer extends PersistentSynchronizer
{
    public PersistentReplacer(DataStore dataStore)
    {
        this(dataStore, false);
    }

    public PersistentReplacer(DataStore dataStore, boolean inTransaction)
    {
        super(dataStore, inTransaction);
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
    protected void handleVersion(
            @Nonnull AssociationEnd associationEnd,
            Object persistentInstance,
            JsonNode jsonNode)
    {
        // TODO: Only increment version if values actually changed
        // TODO: Always deep-fetch versions
        Object versionPersistentInstance = this.dataStore.getToOne(persistentInstance, associationEnd);
        DataTypeProperty versionProperty = associationEnd.getType()
                .getDataTypeProperties()
                .select(DataTypeProperty::isVersion)
                .getOnly();
        Integer versionNumber = (Integer) this.dataStore.getDataTypeProperty(
                versionPersistentInstance,
                versionProperty);
        this.dataStore.setDataTypeProperty(versionPersistentInstance, versionProperty, versionNumber + 1);
    }

    @Override
    protected void handleToOneOutsideProjection(
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance,
            @Nonnull JsonNode incomingChildInstance)
    {
        if (associationEnd.isOwned())
        {
            throw new AssertionError("Assumption is that all owned association ends are inside projection, all unowned are outside projection");
        }

        if (incomingChildInstance.isMissingNode()
                || incomingChildInstance.isNull())
        {
            this.dataStore.setToOne(persistentParentInstance, associationEnd, null);
            return;
        }

        Object childPersistentInstanceAssociated = this.dataStore.getToOne(persistentParentInstance, associationEnd);

        Object childPersistentInstanceWithKey = this.findExistingChildPersistentInstance(
                persistentParentInstance,
                incomingChildInstance,
                associationEnd);
        if (childPersistentInstanceWithKey == null)
        {
            throw new AssertionError();
        }

        if (childPersistentInstanceAssociated == childPersistentInstanceWithKey)
        {
            // TODO: Return a flag indicating nothing changed
            return;
        }

        if (associationEnd.isFinal())
        {
            throw new AssertionError();
        }

        this.dataStore.setToOne(persistentParentInstance, associationEnd, childPersistentInstanceWithKey);
        // TODO: Return a flag indicating a mutation happened
    }

    @Nonnull
    @Override
    protected PersistentSynchronizer determineNextMode(OperationMode nextMode)
    {
        if (nextMode == OperationMode.CREATE)
        {
            return new PersistentCreator(this.dataStore, this.inTransaction);
        }

        if (nextMode == OperationMode.REPLACE)
        {
            return new PersistentReplacer(this.dataStore, this.inTransaction);
        }

        throw new AssertionError();
    }
}
