package cool.klass.reladomo.persistent.writer;

import java.time.Instant;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import org.eclipse.collections.api.list.ImmutableList;

public class PersistentCreator extends PersistentSynchronizer
{
    public PersistentCreator(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore)
    {
        this(mutationContext, dataStore, false);
    }

    public PersistentCreator(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore,
            boolean inTransaction)
    {
        super(mutationContext, dataStore, inTransaction);
    }

    @Override
    protected boolean shouldWriteKey()
    {
        return true;
    }

    @Override
    protected boolean shouldWriteId()
    {
        return false;
    }

    @Override
    protected void synchronizeCreatedDataTypeProperties(@Nonnull Klass klass, Object persistentInstance)
    {
        Optional<PrimitiveProperty> createdByProperty = klass.getCreatedByProperty();
        Optional<PrimitiveProperty> createdOnProperty = klass.getCreatedOnProperty();

        createdByProperty.ifPresent(primitiveProperty ->
        {
            Optional<String> optionalUserId = this.mutationContext.getUserId();
            String userId = optionalUserId.orElseThrow(() -> this.expectAuditProperty(primitiveProperty));
            if (!this.dataStore.setDataTypeProperty(persistentInstance, primitiveProperty, userId))
            {
                throw new AssertionError();
            }
        });

        createdOnProperty.ifPresent(primitiveProperty ->
        {
            Instant transactionTime = this.mutationContext.getTransactionTime();
            if (!this.dataStore.setDataTypeProperty(persistentInstance, primitiveProperty, transactionTime))
            {
                throw new AssertionError();
            }
        });
    }

    private AssertionError expectAuditProperty(PrimitiveProperty primitiveProperty)
    {
        String message = String.format(
                "Mutation context has no userId, but found an audit property: '%s'",
                primitiveProperty);
        return new AssertionError(message);
    }

    @Override
    protected void handleVersion(
            @Nonnull AssociationEnd associationEnd,
            Object persistentInstance)
    {
        Object persistentChildInstance = this.dataStore.getToOne(persistentInstance, associationEnd);
        if (persistentChildInstance != null)
        {
            throw new AssertionError();
        }

        ImmutableList<Object> keys = this.getKeysFromPersistentInstance(
                persistentInstance,
                associationEnd.getOwningClassifier());
        this.insertVersion(persistentInstance, associationEnd, keys);
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
            return false;
        }

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

        this.dataStore.setToOne(persistentParentInstance, associationEnd, childPersistentInstanceWithKey);
        // TODO: Return a flag indicating a mutation happened
        return true;
    }

    private void insertVersion(
            Object persistentInstance,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ImmutableList<Object> keys)
    {
        Klass  versionType     = associationEnd.getType();
        Object versionInstance = this.dataStore.instantiate(versionType, keys);

        // TODO: Test where version association end and version property are not named "version"
        // TODO: Enforce that version association ends are either always or never part of the write projection. Probably always included. Or always infer that they are included.

        // Set version: 1
        DataTypeProperty versionProperty = versionType.getVersionNumberProperty().get();
        this.dataStore.setDataTypeProperty(versionInstance, versionProperty, 1);

        this.synchronizeCreatedDataTypeProperties(versionType, versionInstance);
        this.synchronizeUpdatedDataTypeProperties(versionType, versionInstance);

        // TODO: This is the backwards order from how I used to do it
        this.dataStore.setToOne(persistentInstance, associationEnd, versionInstance);
        this.dataStore.insert(versionInstance);
    }

    @Nonnull
    @Override
    protected PersistentSynchronizer determineNextMode(OperationMode nextMode)
    {
        if (nextMode == OperationMode.CREATE)
        {
            return new PersistentCreator(this.mutationContext, this.dataStore, this.inTransaction);
        }

        throw new AssertionError();
    }
}
