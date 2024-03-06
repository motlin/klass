package cool.klass.reladomo.persistent.writer;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.utility.Iterate;

public class PersistentCreator extends PersistentSynchronizer
{
    public PersistentCreator(DataStore dataStore)
    {
        this(dataStore, false);
    }

    public PersistentCreator(DataStore dataStore, boolean inTransaction)
    {
        super(dataStore, inTransaction);
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
    protected void handleVersion(Object persistentInstance, JsonNode jsonNode, AssociationEnd associationEnd)
    {
        Object persistentChildInstance = this.dataStore.getToOne(persistentInstance, associationEnd);
        if (persistentChildInstance != null)
        {
            throw new AssertionError();
        }

        ImmutableList<Object> keys = getKeysFromPersistentInstance(
                persistentInstance,
                associationEnd.getOwningKlass());
        this.insertVersion(persistentInstance, associationEnd, keys);
    }

    protected void handleToOneOutsideProjection(
            Object persistentParentInstance,
            JsonNode incomingChildInstance,
            AssociationEnd associationEnd)
    {
        if (associationEnd.isOwned())
        {
            throw new AssertionError("Assumption is that all owned association ends are inside projection, all unowned are outside projection");
        }

        if (incomingChildInstance.isMissingNode()
                || incomingChildInstance.isNull())
        {
            return;
        }

        Object childPersistentInstanceWithKey = this.findExistingChildPersistentInstance(
                persistentParentInstance,
                incomingChildInstance,
                associationEnd);
        if (childPersistentInstanceWithKey == null)
        {
            throw new AssertionError();
        }

        this.dataStore.setToOne(persistentParentInstance, associationEnd, childPersistentInstanceWithKey);
        // TODO: Return a flag indicating a mutation happened
    }

    private void insertVersion(
            Object persistentInstance,
            AssociationEnd associationEnd,
            ImmutableList<Object> keys)
    {
        Klass  resultType  = associationEnd.getType();
        Object newInstance = this.dataStore.instantiate(resultType, keys);

        // TODO: Test where version association end and version property are not named "version"
        // TODO: Enforce that version association ends are either always or never part of the write projection. Probably always included. Or always infer that they are included.

        // Set version: 1
        ImmutableList<DataTypeProperty> versionProperties = resultType.getDataTypeProperties().select(DataTypeProperty::isVersion);
        DataTypeProperty                versionProperty   = Iterate.getOnly(versionProperties);
        this.dataStore.setDataTypeProperty(newInstance, versionProperty, 1);

        // TODO: This is the backwards order from how I used to do it
        this.dataStore.setToOne(persistentInstance, associationEnd, newInstance);
        this.dataStore.insert(newInstance);
    }

    @Override
    protected PersistentSynchronizer determineNextMode(OperationMode nextMode)
    {
        if (nextMode == OperationMode.CREATE)
        {
            return new PersistentCreator(this.dataStore, this.inTransaction);
        }

        throw new AssertionError();
    }
}
