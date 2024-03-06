package cool.klass.reladomo.persistent.writer;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class PersistentReplacer extends PersistentSynchronizer
{
    public PersistentReplacer(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore)
    {
        this(mutationContext, dataStore, false);
    }

    public PersistentReplacer(
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
            @Nonnull AssociationEnd associationEnd,
            Object persistentInstance)
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
    protected boolean handleToOneOutsideProjection(
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
            return this.dataStore.setToOne(persistentParentInstance, associationEnd, null);
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
            return false;
        }

        if (associationEnd.isFinal())
        {
            throw new AssertionError();
        }

        return this.dataStore.setToOne(persistentParentInstance, associationEnd, childPersistentInstanceWithKey);
    }

    @Nonnull
    @Override
    protected PersistentSynchronizer determineNextMode(OperationMode nextMode)
    {
        if (nextMode == OperationMode.CREATE)
        {
            return new PersistentCreator(this.mutationContext, this.dataStore, this.inTransaction);
        }

        if (nextMode == OperationMode.REPLACE)
        {
            return new PersistentReplacer(this.mutationContext, this.dataStore, this.inTransaction);
        }

        throw new AssertionError();
    }
}
