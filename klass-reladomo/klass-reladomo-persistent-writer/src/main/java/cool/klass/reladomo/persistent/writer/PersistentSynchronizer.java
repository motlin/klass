package cool.klass.reladomo.persistent.writer;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.JsonDataTypeValueVisitor;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.partition.list.PartitionImmutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class PersistentSynchronizer
{
    @Nonnull
    protected final MutationContext mutationContext;
    @Nonnull
    protected final DataStore       dataStore;

    // TODO: Consider getting rid of this, it's just for assertions
    protected boolean inTransaction;

    protected PersistentSynchronizer(
            @Nonnull MutationContext mutationContext,
            @Nonnull DataStore dataStore,
            boolean inTransaction)
    {
        this.mutationContext = Objects.requireNonNull(mutationContext);
        this.dataStore = Objects.requireNonNull(dataStore);
        this.inTransaction = inTransaction;
    }

    protected abstract boolean shouldWriteKey();

    protected abstract boolean shouldWriteId();

    public boolean synchronize(
            @Nonnull Klass klass,
            Object persistentInstance,
            @Nonnull ObjectNode incomingJson)
    {
        if (this.inTransaction)
        {
            throw new AssertionError();
        }

        return this.dataStore.runInTransaction(transaction ->
        {
            Instant transactionTime       = this.mutationContext.getTransactionTime();
            long    transactionTimeMillis = transactionTime.toEpochMilli();
            transaction.setSystemTime(transactionTimeMillis);

            this.inTransaction = true;
            try
            {
                return this.synchronizeInTransaction(klass, Optional.empty(), persistentInstance, incomingJson);
            }
            finally
            {
                this.inTransaction = false;
            }
        });
    }

    // TODO: Get rid of this
    public void synchronizeWithoutStartingTransaction(
            @Nonnull Klass klass,
            Object persistentInstance,
            @Nonnull ObjectNode incomingJson)
    {
        if (this.inTransaction)
        {
            throw new AssertionError();
        }

        this.inTransaction = true;
        try
        {
            this.synchronizeInTransaction(klass, Optional.empty(), persistentInstance, incomingJson);
        }
        finally
        {
            this.inTransaction = false;
        }
    }

    protected boolean synchronizeInTransaction(
            @Nonnull Klass klass,
            @Nonnull Optional<AssociationEnd> pathHere,
            Object persistentInstance,
            @Nonnull ObjectNode incomingJson)
    {
        if (!this.inTransaction)
        {
            throw new AssertionError();
        }

        boolean propertyMutationOccurred = false;
        if (!this.isRestrictedFromWriting(klass))
        {
            propertyMutationOccurred |= this.synchronizeDataTypeProperties(klass, persistentInstance, incomingJson);
        }
        boolean associatedMutationOccurred = this.synchronizeAssociationEnds(
                klass,
                pathHere,
                persistentInstance,
                incomingJson);
        boolean mutationOccurred = propertyMutationOccurred || associatedMutationOccurred;

        if (propertyMutationOccurred)
        {
            this.synchronizeUpdatedDataTypeProperties(klass, persistentInstance);
        }

        if (mutationOccurred)
        {
            // TODO: Bump version number and version audit properties
            klass.getVersionProperty()
                    .ifPresent(associationEnd -> this.handleVersion(associationEnd, persistentInstance));
        }

        return mutationOccurred;
    }

    protected void synchronizeUpdatedDataTypeProperties(Klass klass, Object persistentInstance)
    {
        Optional<PrimitiveProperty> lastUpdatedByProperty = klass.getLastUpdatedByProperty();
        lastUpdatedByProperty.ifPresent(primitiveProperty ->
        {
            Optional<String> optionalUserId = this.mutationContext.getUserId();
            String           userId         = optionalUserId.orElseThrow(() -> new AssertionError(primitiveProperty));
            this.dataStore.setDataTypeProperty(persistentInstance, primitiveProperty, userId);
        });
    }

    //region DataTypeProperties
    private boolean isRestrictedFromWriting(@Nonnull Klass klass)
    {
        return klass.isTransient() || klass.getVersionedProperty().isPresent();
    }

    protected boolean synchronizeDataTypeProperties(
            @Nonnull Klass klass,
            Object persistentInstance,
            @Nonnull ObjectNode incomingJson)
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = klass.getDataTypeProperties();
        ImmutableList<DataTypeProperty> simpleDataTypeProperties = dataTypeProperties.rejectWith(
                this::shouldSkipDataTypeProperty,
                klass);

        boolean mutationOccurred = false;
        for (DataTypeProperty dataTypeProperty : simpleDataTypeProperties)
        {
            mutationOccurred |= this.synchronizeDataTypeProperty(dataTypeProperty, persistentInstance, incomingJson);
        }

        this.synchronizeCreatedDataTypeProperties(klass, persistentInstance);

        return mutationOccurred;
    }

    protected abstract void synchronizeCreatedDataTypeProperties(Klass klass, Object persistentInstance);

    private boolean synchronizeDataTypeProperty(
            DataTypeProperty dataTypeProperty,
            Object persistentInstance,
            @Nonnull ObjectNode incomingJson)
    {
        Object newValue = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(dataTypeProperty, incomingJson);
        return this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, newValue);
    }

    private boolean shouldSkipDataTypeProperty(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull Klass klass)
    {
        return dataTypeProperty.isForeignKey()
                || dataTypeProperty.isAudit()
                || dataTypeProperty.isTemporal()
                || this.hasReferencePropertyDependentOnDataTypeProperty(klass, dataTypeProperty)
                || dataTypeProperty.isKey() && !this.shouldWriteKey()
                || dataTypeProperty.isID() && !this.shouldWriteId();
    }
    //endregion

    //region AssociationEnds
    private boolean synchronizeAssociationEnds(
            @Nonnull Klass klass,
            @Nonnull Optional<AssociationEnd> pathHere,
            Object persistentInstance,
            @Nonnull ObjectNode incomingObjectNode)
    {
        PartitionImmutableList<AssociationEnd> forwardOwnedAssociationEnds = klass.getAssociationEnds()
                .reject(associationEnd -> pathHere.equals(Optional.of(associationEnd.getOpposite())))
                .reject(AssociationEnd::isVersion)
                .partition(AssociationEnd::isOwned);

        boolean mutationOccurred = false;
        for (AssociationEnd associationEnd : forwardOwnedAssociationEnds.getSelected())
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();

            JsonNode jsonNode = incomingObjectNode.path(associationEnd.getName());
            if (multiplicity.isToOne())
            {
                mutationOccurred |= this.handleToOne(associationEnd, persistentInstance, jsonNode);
            }
            else
            {
                mutationOccurred |= this.handleToMany(associationEnd, persistentInstance, jsonNode);
            }
        }

        for (AssociationEnd associationEnd : forwardOwnedAssociationEnds.getRejected())
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();
            JsonNode     jsonNode     = incomingObjectNode.path(associationEnd.getName());

            if (multiplicity.isToOne())
            {
                mutationOccurred |= this.handleToOneOutsideProjection(associationEnd, persistentInstance, jsonNode);
            }
            else
            {
                mutationOccurred |= this.handleToManyOutsideProjection(associationEnd, persistentInstance, jsonNode);
            }
        }
        return mutationOccurred;
    }

    protected abstract void handleVersion(
            AssociationEnd associationEnd,
            Object persistentInstance);

    private boolean handleToOne(
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance,
            @Nonnull JsonNode incomingChildInstance)
    {
        Object persistentChildInstance = this.dataStore.getToOne(persistentParentInstance, associationEnd);

        if (persistentChildInstance == null
                && !incomingChildInstance.isMissingNode()
                && !incomingChildInstance.isNull())
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingChildInstance,
                    associationEnd,
                    persistentParentInstance);
            this.insert(associationEnd, persistentParentInstance, incomingChildInstance, keys);
            return true;
        }

        if (persistentChildInstance != null
                && incomingChildInstance.isMissingNode()
                || incomingChildInstance.isNull())
        {
            this.deleteOrTerminate(associationEnd.getType(), persistentChildInstance);
            return true;
        }

        if (persistentChildInstance != null && incomingChildInstance != null)
        {
            PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.REPLACE);
            return synchronizer.synchronize(
                    associationEnd.getType(),
                    persistentChildInstance,
                    (ObjectNode) incomingChildInstance);
        }

        return false;
    }

    protected abstract boolean handleToOneOutsideProjection(
            AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstance);

    protected Object findExistingChildPersistentInstance(
            Object persistentParentInstance,
            @Nonnull JsonNode incomingChildInstance,
            @Nonnull AssociationEnd associationEnd)
    {
        if (!(this instanceof PersistentCreator) && !(this instanceof PersistentReplacer))
        {
            throw new AssertionError();
        }
        ImmutableList<Object> keys = this.getKeysFromJsonNode(
                incomingChildInstance,
                associationEnd,
                persistentParentInstance);
        return this.dataStore.findByKey(associationEnd.getType(), keys);
    }

    private void insert(
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance,
            JsonNode incomingChildInstance,
            ImmutableList<Object> keys)
    {
        Klass                  resultType   = associationEnd.getType();
        Object                 newInstance  = this.dataStore.instantiate(resultType, keys);
        PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.CREATE);
        boolean mutationOccurred = synchronizer.synchronizeInTransaction(
                associationEnd.getType(),
                Optional.of(associationEnd),
                newInstance,
                (ObjectNode) incomingChildInstance);
        if (!mutationOccurred)
        {
            throw new AssertionError();
        }
        // TODO: This is the backwards order from how I used to do it
        this.dataStore.setToOne(persistentParentInstance, associationEnd, newInstance);
        this.dataStore.insert(newInstance);
    }

    private void deleteOrTerminate(Klass klass, @Nonnull Object persistentInstance)
    {
        ReladomoPersistentDeleter reladomoPersistentDeleter = new ReladomoPersistentDeleter(this.dataStore);
        reladomoPersistentDeleter.deleteOrTerminate(klass, persistentInstance);
    }

    private boolean handleToMany(
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance,
            @Nonnull JsonNode incomingChildInstances)
    {
        boolean mutationOccurred = false;
        // TODO: Test null where an array goes

        ImmutableList<JsonNode> incomingInstancesForUpdate = Lists.immutable.withAll(incomingChildInstances)
                .rejectWith(
                        this::jsonNodeNeedsIdInferredOnInsert,
                        associationEnd);

        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                incomingInstancesForUpdate,
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
                reladomoPersistentDeleter.deleteOrTerminate(
                        associationEnd.getType(),
                        persistentChildInstance);
                mutationOccurred = true;
            }
        }

        // Do a second query for the same list, but without the instances we just terminated
        List<Object> nonTerminatedPersistentChildInstances = this.dataStore.getToMany(
                persistentParentInstance,
                associationEnd);

        MapIterable<ImmutableList<Object>, Object> persistentChildInstancesByKey = this.indexPersistentInstances(
                nonTerminatedPersistentChildInstances,
                associationEnd.getType());

        for (JsonNode incomingChildInstance : incomingChildInstances)
        {
            Object persistentChildInstance = this.getPersistentChildInstance(
                    associationEnd,
                    persistentParentInstance,
                    persistentChildInstancesByKey,
                    incomingChildInstance);
            if (persistentChildInstance == null)
            {
                if (!associationEnd.isOwned())
                {
                    throw new AssertionError();
                }

                Klass resultType = associationEnd.getType();
                ImmutableList<Object> keys = this.getKeysFromJsonNode(
                        incomingChildInstance,
                        associationEnd,
                        persistentParentInstance);
                Object newInstance = this.dataStore.instantiate(resultType, keys);

                this.dataStore.setToOne(newInstance, associationEnd.getOpposite(), persistentParentInstance);

                PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.CREATE);
                boolean result = synchronizer.synchronizeInTransaction(
                        associationEnd.getType(),
                        Optional.of(associationEnd),
                        newInstance,
                        (ObjectNode) incomingChildInstance);
                if (!result)
                {
                    throw new AssertionError();
                }

                this.dataStore.insert(newInstance);
                mutationOccurred = true;
            }
            else
            {
                PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.REPLACE);
                mutationOccurred |= synchronizer.synchronizeInTransaction(
                        associationEnd.getType(),
                        Optional.of(associationEnd),
                        persistentChildInstance,
                        (ObjectNode) incomingChildInstance);
            }
        }

        return mutationOccurred;
    }

    @Nullable
    private Object getPersistentChildInstance(
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance,
            @Nonnull MapIterable<ImmutableList<Object>, Object> persistentChildInstancesByKey,
            @Nonnull JsonNode incomingChildInstance)
    {
        if (this.jsonNodeNeedsIdInferredOnInsert(incomingChildInstance, associationEnd))
        {
            return null;
        }

        ImmutableList<Object> keys = this.getKeysFromJsonNode(
                incomingChildInstance,
                associationEnd,
                persistentParentInstance);

        return persistentChildInstancesByKey.get(keys);
    }

    private boolean handleToManyOutsideProjection(
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance,
            @Nonnull JsonNode incomingChildInstances)
    {
        boolean mutationOccurred = false;

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
                mutationOccurred = synchronizer.synchronizeInTransaction(
                        associationEnd.getType(),
                        Optional.of(associationEnd),
                        persistentChildInstance,
                        (ObjectNode) incomingChildInstance);
            }
        }

        return mutationOccurred;
    }

    @Nonnull
    private MapIterable<ImmutableList<Object>, JsonNode> indexIncomingJsonInstances(
            @Nonnull Iterable<JsonNode> incomingInstances,
            @Nonnull AssociationEnd associationEnd,
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

    protected ImmutableList<Object> getKeysFromPersistentInstance(Object persistentInstance, @Nonnull Klass klass)
    {
        return klass
                .getKeyProperties()
                .collect(keyProperty -> this.dataStore.getDataTypeProperty(persistentInstance, keyProperty));
    }

    @Nonnull
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
    //endregion

    private Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.get(opposite);

        if (oppositeForeignKeys.notEmpty())
        {
            Object result = this.dataStore.getDataTypeProperty(
                    persistentParentInstance,
                    oppositeForeignKeys.getOnly());
            return Objects.requireNonNull(result);
        }

        if (keysMatchingThisForeignKey.notEmpty())
        {
            if (keysMatchingThisForeignKey.size() != 1)
            {
                throw new AssertionError();
            }

            Pair<AssociationEnd, DataTypeProperty> pair = keysMatchingThisForeignKey.keyValuePairsView().getOnly();

            JsonNode childNode = jsonNode.path(pair.getOne().getName());
            Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                    pair.getTwo(),
                    (ObjectNode) childNode);
            return Objects.requireNonNull(result);
        }

        Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                keyProperty,
                (ObjectNode) jsonNode);
        return Objects.requireNonNull(result);
    }

    @Nonnull
    private MapIterable<ImmutableList<Object>, Object> indexPersistentInstances(
            @Nonnull List<Object> persistentInstances,
            @Nonnull Klass klass)
    {
        // TODO: Change to use groupByUniqueKey after EC 10.0 is released.

        MutableOrderedMap<ImmutableList<Object>, Object> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (Object persistentInstance : persistentInstances)
        {
            ImmutableList<Object> keysFromPersistentInstance = this.getKeysFromPersistentInstance(
                    persistentInstance,
                    klass);
            result.put(keysFromPersistentInstance, persistentInstance);
        }

        return result;
        // TODO: Change to use asUnmodifiable after EC 10.0 is released.
        // return result.asUnmodifiable();
    }

    private boolean jsonNodeNeedsIdInferredOnInsert(
            JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd)
    {
        return associationEnd
                .getType()
                .getKeyProperties()
                .allSatisfy(keyProperty -> this.jsonNodeNeedsIdInferredOnInsert(
                        keyProperty,
                        jsonNode,
                        associationEnd));
    }

    private ImmutableList<Object> getKeysFromJsonNode(
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            Object persistentParentInstance)
    {
        if (this.jsonNodeNeedsIdInferredOnInsert(jsonNode, associationEnd))
        {
            return Lists.immutable.empty();
        }

        return associationEnd.getType()
                .getKeyProperties()
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode,
                        associationEnd,
                        persistentParentInstance));
    }

    private boolean jsonNodeNeedsIdInferredOnInsert(
            @Nonnull DataTypeProperty keyProperty,
            JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.get(opposite);

        if (oppositeForeignKeys.notEmpty())
        {
            return false;
        }

        if (keysMatchingThisForeignKey.notEmpty())
        {
            if (keysMatchingThisForeignKey.size() != 1)
            {
                throw new AssertionError();
            }

            return false;
        }

        return JsonDataTypeValueVisitor.dataTypePropertyIsNullInJson(
                keyProperty,
                (ObjectNode) jsonNode);
    }
}
