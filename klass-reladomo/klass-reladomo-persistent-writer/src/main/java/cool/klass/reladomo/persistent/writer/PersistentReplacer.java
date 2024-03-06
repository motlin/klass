package cool.klass.reladomo.persistent.writer;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

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
    protected void synchronizeUpdatedDataTypeProperties(
            @Nonnull Klass klass,
            Object persistentInstance,
            boolean propertyMutationOccurred)
    {
        if (propertyMutationOccurred)
        {
            this.synchronizeUpdatedDataTypeProperties(klass, persistentInstance);
        }
    }

    @Override
    protected void validateSetIdDataTypeProperties(Klass klass, Object persistentInstance)
    {
        // Deliberately empty for update operation
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
            @Nonnull Object persistentParentInstance,
            @Nonnull ObjectNode incomingParentNode,
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
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingChildInstance,
                    associationEnd,
                    persistentParentInstance);
            String error = String.format("Could not find existing %s with key %s", associationEnd.getType(), keys);
            // TODO: Error message including full path here. Error message earlier, during validation.
            // It's possible to trigger this code path by deleting reference data from tests, like one of the Tags listed in test-data/create-blueprint.txt
            throw new IllegalStateException(error);
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
