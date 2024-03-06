package cool.klass.reladomo.persistent.writer;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class PersistentPatcher
        extends PersistentSynchronizer
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
    protected boolean synchronizeDataTypeProperty(
            @Nonnull DataTypeProperty dataTypeProperty,
            Object persistentInstance,
            @Nonnull ObjectNode incomingJson)
    {
        Object newValue = this.mutationContext.getPropertyDataFromUrl().get(dataTypeProperty);
        if (newValue != null)
        {
            return this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, newValue);
        }

        JsonNode jsonDataTypeValue = incomingJson.path(dataTypeProperty.getName());
        if (jsonDataTypeValue.isMissingNode())
        {
            return false;
        }

        return super.synchronizeDataTypeProperty(dataTypeProperty, persistentInstance, incomingJson);
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
            @Nonnull AssociationEnd associationEnd,
            @Nonnull Object persistentParentInstance,
            @Nonnull ObjectNode incomingParentNode,
            @Nonnull JsonNode incomingChildInstance)
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
