package cool.klass.reladomo.persistent.writer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.JsonDataTypeValueVisitor;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.partition.list.PartitionImmutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class PersistentSynchronizer
{
    @Nonnull
    protected final DataStore dataStore;

    // TODO: Consider getting rid of this, it's just for assertions
    protected boolean inTransaction;

    protected PersistentSynchronizer(DataStore dataStore, boolean inTransaction)
    {
        this.dataStore = Objects.requireNonNull(dataStore);
        this.inTransaction = inTransaction;
    }

    protected abstract boolean shouldWriteKey();

    protected abstract boolean shouldWriteId();

    public void synchronize(
            Klass klass,
            Object persistentInstance,
            ObjectNode incomingJson)
    {
        if (this.inTransaction)
        {
            throw new AssertionError();
        }

        this.dataStore.runInTransaction(() ->
        {
            this.inTransaction = true;
            try
            {
                this.synchronizeInTransaction(klass, Optional.empty(), persistentInstance, incomingJson);
            }
            finally
            {
                this.inTransaction = false;
            }
        });
    }

    protected void synchronizeInTransaction(
            Klass klass,
            Optional<AssociationEnd> pathHere,
            Object persistentInstance,
            ObjectNode incomingJson)
    {
        if (!this.inTransaction)
        {
            throw new AssertionError();
        }

        if (!this.isRestrictedFromWriting(klass))
        {
            this.synchronizeDataTypeProperties(klass, persistentInstance, incomingJson);
        }
        this.synchronizeAssociationEnds(klass, pathHere, persistentInstance, incomingJson);
    }

    private boolean isRestrictedFromWriting(Klass klass)
    {
        return klass.isTransient() || klass.getVersionedProperty().isPresent();
    }

    private void synchronizeDataTypeProperties(
            Klass klass,
            Object persistentInstance,
            ObjectNode incomingJson)
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = klass.getDataTypeProperties();
        ImmutableList<DataTypeProperty> nonDerivedDataTypeProperties = this.getNonDerivedDataTypeProperties(dataTypeProperties);
        for (int i = 0; i < nonDerivedDataTypeProperties.size(); i++)
        {
            DataTypeProperty dataTypeProperty = nonDerivedDataTypeProperties.get(i);

            // Skip `Long key id` properties in create (POST) mode.
            // Skip temporal properties in most modes
            // Handle audit properties separately
            // Skip foreign keys for associations within the Projection.

            if (dataTypeProperty.isForeignKey()
                    || dataTypeProperty.isAudit()
                    || dataTypeProperty.isTemporal()
                    || this.hasReferencePropertyDependentOnDataTypeProperty(klass, dataTypeProperty)
                    || dataTypeProperty.isKey() && !this.shouldWriteKey()
                    || dataTypeProperty.isID() && !this.shouldWriteId())
            {
                continue;
            }

            Object newValue = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(dataTypeProperty, incomingJson);
            // TODO: Reorder the first two parameters
            this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, newValue);
        }

        this.handleForeignKeysForAssociationsOutsideProjection(persistentInstance, incomingJson, klass);
    }

    private void synchronizeAssociationEnds(
            Klass klass,
            Optional<AssociationEnd> pathHere,
            Object persistentInstance,
            ObjectNode incomingObjectNode)
    {
        PartitionImmutableList<AssociationEnd> forwardOwnedAssociationEnds = klass.getAssociationEnds()
                .reject(associationEnd -> pathHere.equals(Optional.of(associationEnd.getOpposite())))
                .partition(AssociationEnd::isOwned);

        for (AssociationEnd associationEnd : forwardOwnedAssociationEnds.getSelected())
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();

            JsonNode jsonNode = incomingObjectNode.path(associationEnd.getName());
            if (associationEnd.isVersion())
            {
                this.handleVersion(associationEnd, persistentInstance, jsonNode);
            }
            else if (multiplicity.isToOne())
            {
                this.handleToOne(associationEnd, persistentInstance, jsonNode);
            }
            else
            {
                this.handleToMany(associationEnd, persistentInstance, jsonNode);
            }
        }

        for (AssociationEnd associationEnd : forwardOwnedAssociationEnds.getRejected())
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();
            JsonNode     jsonNode     = incomingObjectNode.path(associationEnd.getName());

            if (associationEnd.isVersion())
            {
                this.handleVersion(associationEnd, persistentInstance, jsonNode);
            }
            else if (multiplicity.isToOne())
            {
                this.handleToOneOutsideProjection(associationEnd, persistentInstance, jsonNode);
            }
            else
            {
                this.handleToManyOutsideProjection(associationEnd, persistentInstance, jsonNode);
            }
        }
    }

    protected abstract void handleVersion(
            AssociationEnd associationEnd,
            Object persistentInstance,
            JsonNode jsonNode);

    private void handleToOne(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstance)
    {
        Object         persistentChildInstance = this.dataStore.getToOne(persistentParentInstance, associationEnd);

        if (persistentChildInstance == null
                && !incomingChildInstance.isMissingNode()
                && !incomingChildInstance.isNull())
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingChildInstance,
                    associationEnd,
                    persistentParentInstance);
            this.insert(associationEnd, persistentParentInstance, incomingChildInstance, keys);
        }
        else if (persistentChildInstance != null
                && incomingChildInstance.isMissingNode()
                || incomingChildInstance.isNull())
        {
            this.deleteOrTerminate(associationEnd.getType(), persistentChildInstance);
        }
        else if (persistentChildInstance != null && incomingChildInstance != null)
        {
            PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.REPLACE);
            synchronizer.synchronize(
                    associationEnd.getType(),
                    persistentChildInstance,
                    (ObjectNode) incomingChildInstance);
        }
    }

    protected abstract void handleToOneOutsideProjection(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstance);

    protected Object findExistingChildPersistentInstance(
            Object persistentParentInstance,
            JsonNode incomingChildInstance,
            AssociationEnd associationEnd)
    {
        if (!(this instanceof PersistentCreator) && !(this instanceof PersistentReplacer))
        {
            throw new AssertionError();
        }
        ImmutableList<Object> keys = this.getKeysFromJsonNode(incomingChildInstance, associationEnd, persistentParentInstance);
        return this.dataStore.findByKey(associationEnd.getType(), keys);
    }

    private void insert(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstance,
            ImmutableList<Object> keys)
    {
        Klass          resultType     = associationEnd.getType();
        Object         newInstance    = this.dataStore.instantiate(resultType, keys);
        PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.CREATE);
        synchronizer.synchronizeInTransaction(
                associationEnd.getType(),
                Optional.of(associationEnd),
                newInstance,
                (ObjectNode) incomingChildInstance);
        // TODO: This is the backwards order from how I used to do it
        this.dataStore.setToOne(persistentParentInstance, associationEnd, newInstance);
        this.dataStore.insert(newInstance);
    }

    private void deleteOrTerminate(Klass klass, Object persistentChildInstance)
    {
        ReladomoPersistentDeleter reladomoPersistentDeleter = new ReladomoPersistentDeleter(this.dataStore);
        reladomoPersistentDeleter.deleteOrTerminate(klass, persistentChildInstance);
    }

    private void handleToMany(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstances)
    {
        // TODO: Test null where an array goes

        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                incomingChildInstances,
                associationEnd,
                persistentParentInstance);

        List<Object> persistentChildInstances = this.dataStore.getToMany(persistentParentInstance, associationEnd);
        for (Object persistentChildInstance : persistentChildInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromPersistentInstance(
                    persistentChildInstance,
                    associationEnd.getType());
            if (!incomingChildInstancesByKey.containsKey(keys))
            {
                ReladomoPersistentDeleter reladomoPersistentDeleter = new ReladomoPersistentDeleter(this.dataStore);
                reladomoPersistentDeleter.deleteOrTerminate(associationEnd.getType(), persistentChildInstance);
            }
        }

        // Do a second query for the same list, but without the instances we just terminated
        List<Object> nonTerminatedPersistentChildInstances = this.dataStore.getToMany(persistentParentInstance, associationEnd);
        MapIterable<ImmutableList<Object>, Object> persistentChildInstancesByKey = this.indexPersistentInstances(nonTerminatedPersistentChildInstances, associationEnd.getType());

        for (JsonNode incomingChildInstance : incomingChildInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingChildInstance,
                    associationEnd,
                    persistentParentInstance);
            Object persistentChildInstance = persistentChildInstancesByKey.get(keys);
            if (persistentChildInstance == null)
            {
                if (!associationEnd.isOwned())
                {
                    throw new AssertionError();
                }

                Klass  resultType  = associationEnd.getType();
                Object newInstance = this.dataStore.instantiate(resultType, keys);

                this.dataStore.setToOne(newInstance, associationEnd.getOpposite(), persistentParentInstance);

                PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.CREATE);
                synchronizer.synchronizeInTransaction(
                        associationEnd.getType(),
                        Optional.of(associationEnd),
                        newInstance,
                        (ObjectNode) incomingChildInstance);

                this.dataStore.insert(newInstance);
            }
            else
            {
                PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.REPLACE);
                synchronizer.synchronizeInTransaction(
                        associationEnd.getType(),
                        Optional.of(associationEnd),
                        persistentChildInstance,
                        (ObjectNode) incomingChildInstance);
            }
        }
    }

    private void handleToManyOutsideProjection(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstances)
    {
        // TODO: Test null where an array goes

        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                incomingChildInstances,
                associationEnd,
                persistentParentInstance);

        List<Object> persistentChildInstances = this.dataStore.getToMany(persistentParentInstance, associationEnd);
        for (Object persistentChildInstance : persistentChildInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromPersistentInstance(
                    persistentChildInstance,
                    associationEnd.getType());
            if (!incomingChildInstancesByKey.containsKey(keys))
            {
                throw new AssertionError();
                // ReladomoPersistentDeleter reladomoPersistentDeleter = new ReladomoPersistentDeleter(this.dataStore);
                // reladomoPersistentDeleter.deleteOrTerminate(persistentChildInstance, associationEnd);
            }
        }

        MapIterable<ImmutableList<Object>, Object> persistentChildInstancesByKey = this.indexPersistentInstances(
                persistentChildInstances,
                associationEnd.getType());

        for (JsonNode incomingChildInstance : incomingChildInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingChildInstance,
                    associationEnd,
                    persistentParentInstance);

            Object persistentChildInstance = persistentChildInstancesByKey.get(keys);
            if (persistentChildInstance == null)
            {
                Klass  resultType  = associationEnd.getType();
                Object newInstance = this.dataStore.instantiate(resultType, keys);

                if (associationEnd.isOwned())
                {
                    throw new AssertionError();
                    // this.dataStore.generateAndSetId(newInstance, resultType);
                    // // TODO: This is the backwards order from how I used to do it
                    // this.dataStore.setToOne(newInstance, associationEnd.getOpposite(), persistentParentInstance);
                    // // TODO: Determine next mode
                    // OperationMode nextMode = this.determineNextMode(mode, OperationMode.CREATE);
                    // this.synchronize(newInstance, (ObjectNode) incomingChildInstance, associationEnd, nextMode);
                    // this.dataStore.insert(newInstance);
                }
                else
                {
                    throw new AssertionError();
                }
            }
            else
            {
                PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.REPLACE);
                // this.synchronize(persistentChildInstance, (ObjectNode) incomingChildInstance, associationEnd, nextMode);
                throw new AssertionError();
            }
        }
    }

    private ImmutableList<Object> getKeysFromJsonNode(
            JsonNode jsonNode,
            AssociationEnd associationEnd,
            Object persistentParentInstance)
    {
        return associationEnd.getType()
                .getKeyProperties()
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode,
                        associationEnd,
                        persistentParentInstance));
    }

    private Object getKeyFromJsonNode(
            DataTypeProperty keyProperty,
            JsonNode jsonNode,
            AssociationEnd associationEnd,
            Object persistentParentInstance)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.get(opposite);

        if (oppositeForeignKeys.notEmpty())
        {
            return this.dataStore.getDataTypeProperty(
                    persistentParentInstance,
                    oppositeForeignKeys.getOnly());
        }

        if (keysMatchingThisForeignKey.notEmpty())
        {
            if (keysMatchingThisForeignKey.size() != 1)
            {
                throw new AssertionError();
            }

            Pair<AssociationEnd, DataTypeProperty> pair = keysMatchingThisForeignKey.keyValuePairsView().getOnly();

            JsonNode childNode = jsonNode.path(pair.getOne().getName());
            return JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                    pair.getTwo(),
                    (ObjectNode) childNode);
        }

        return JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                keyProperty,
                (ObjectNode) jsonNode);
    }

    private MapIterable<ImmutableList<Object>, Object> indexPersistentInstances(
            List<Object> persistentInstances,
            Klass klass)
    {
        // TODO: Change to use groupByUniqueKey after EC 10.0 is released.

        MutableOrderedMap<ImmutableList<Object>, Object> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (Object persistentInstance : persistentInstances)
        {
            ImmutableList<Object> keysFromPersistentInstance = this.getKeysFromPersistentInstance(persistentInstance, klass);
            result.put(keysFromPersistentInstance, persistentInstance);
        }

        return result;
        // TODO: Change to use asUnmodifiable after EC 10.0 is released.
        // return result.asUnmodifiable();
    }

    private MapIterable<ImmutableList<Object>, JsonNode> indexIncomingJsonInstances(
            JsonNode incomingInstances,
            AssociationEnd associationEnd,
            Object persistentParentInstance)
    {
        MutableOrderedMap<ImmutableList<Object>, JsonNode> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (JsonNode incomingInstance : incomingInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingInstance,
                    associationEnd,
                    persistentParentInstance);
            result.put(keys, incomingInstance);
        }
        return result;
        // TODO: Change to use asUnmodifiable after EC 10.0 is released.
        // return result.asUnmodifiable();
    }

    protected ImmutableList<Object> getKeysFromPersistentInstance(Object persistentInstance, Klass klass)
    {
        return klass
                .getKeyProperties()
                .collect(keyProperty -> this.dataStore.getDataTypeProperty(persistentInstance, keyProperty));
    }

    protected abstract PersistentSynchronizer determineNextMode(OperationMode nextMode);

    private boolean hasReferencePropertyDependentOnDataTypeProperty(
            Klass klass,
            DataTypeProperty dataTypeProperty)
    {
        return false;
    }

    private ImmutableList<DataTypeProperty> getNonDerivedDataTypeProperties(ImmutableList<DataTypeProperty> dataTypeProperties)
    {
        return dataTypeProperties;
    }

    private void handleForeignKeysForAssociationsOutsideProjection(
            Object persistentInstance,
            ObjectNode incomingJson,
            Klass klass)
    {
    }
}
