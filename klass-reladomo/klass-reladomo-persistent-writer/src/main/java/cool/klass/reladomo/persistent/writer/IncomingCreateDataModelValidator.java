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
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.reladomo.persistent.writer.context.AssociationEndErrorContext;
import cool.klass.reladomo.persistent.writer.context.AssociationEndWithIndexErrorContext;
import cool.klass.reladomo.persistent.writer.context.DataTypePropertyErrorContext;
import cool.klass.reladomo.persistent.writer.context.ErrorContext;
import cool.klass.reladomo.persistent.writer.context.KlassErrorContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.partition.list.PartitionImmutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class IncomingCreateDataModelValidator
{
    @Nonnull
    private final DataStore                  dataStore;
    @Nonnull
    private final Klass                      klass;
    @Nonnull
    private final ObjectNode                 incomingJson;
    @Nonnull
    private final MutableList<String>        errors;
    @Nonnull
    private final MutableStack<ErrorContext> errorContextStack;
    @Nonnull
    private final MutableStack<String>       contextStack;
    @Nonnull
    private final Optional<AssociationEnd>   pathHere;
    private final boolean                    isRoot;

    public IncomingCreateDataModelValidator(
            @Nonnull DataStore dataStore,
            @Nonnull Klass klass,
            @Nonnull ObjectNode incomingJson,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableStack<ErrorContext> errorContextStack,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        this.dataStore = Objects.requireNonNull(dataStore);
        this.klass = Objects.requireNonNull(klass);
        this.incomingJson = Objects.requireNonNull(incomingJson);
        this.errors = Objects.requireNonNull(errors);
        this.errorContextStack = Objects.requireNonNull(errorContextStack);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.pathHere = Objects.requireNonNull(pathHere);
        this.isRoot = isRoot;
    }

    public static void synchronize(
            DataStore dataStore,
            Klass klass,
            ObjectNode incomingJson,
            MutableList<String> errors)
    {
        IncomingCreateDataModelValidator validator = new IncomingCreateDataModelValidator(
                dataStore,
                klass,
                incomingJson,
                errors,
                Stacks.mutable.empty(),
                Stacks.mutable.empty(),
                Optional.empty(),
                true);
        validator.synchronize();
    }

    public void synchronize()
    {
        if (this.isRoot)
        {
            ImmutableList<Object> keysFromJsonNode = this.getKeysFromJsonNode(this.incomingJson, this.klass);
            this.errorContextStack.push(new KlassErrorContext(this.klass, keysFromJsonNode));
            this.contextStack.push(this.klass.getName());
        }
        try
        {
            this.synchronizeDataTypeProperties(this.klass, this.incomingJson);
            this.synchronizeAssociationEnds(this.klass, this.pathHere, this.incomingJson);
        }
        finally
        {
            if (this.isRoot)
            {
                this.errorContextStack.pop();
                this.contextStack.pop();
            }
        }
    }

    //region DataTypeProperties
    private void synchronizeDataTypeProperties(
            @Nonnull Klass klass,
            @Nonnull ObjectNode incomingJson)
    {
        ImmutableList<DataTypeProperty> plainProperties = klass.getDataTypeProperties()
                .reject(DataTypeProperty::isID)
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isTemporal)
                .reject(DataTypeProperty::isAudit)
                .reject(DataTypeProperty::isForeignKey);

        this.handleIdProperties(klass.getDataTypeProperties().select(DataTypeProperty::isID));
        this.checkPresentPropertiesMatch(klass.getDataTypeProperties().select(DataTypeProperty::isTemporal));
        this.checkPresentPropertiesMatch(klass.getDataTypeProperties().select(DataTypeProperty::isAudit));
        this.checkRequiredPropertiesPresent(plainProperties);
    }

    private void handleIdProperties(@Nonnull ImmutableList<DataTypeProperty> idProperties)
    {
        this.checkPresentPropertiesMatch(idProperties);
        if (!this.isRoot)
        {
            this.checkRequiredPropertiesPresent(idProperties);
        }
    }

    private void checkPresentPropertiesMatch(@Nonnull ImmutableList<DataTypeProperty> properties)
    {
        for (DataTypeProperty dataTypeProperty : properties)
        {
            this.errorContextStack.push(new DataTypePropertyErrorContext(dataTypeProperty));
            this.contextStack.push(dataTypeProperty.getName());

            try
            {
                JsonNode jsonNode = this.incomingJson.path(dataTypeProperty.getName());
                if (!jsonNode.isMissingNode() && !jsonNode.isNull())
                {
                    if (dataTypeProperty.isTemporal())
                    {
                        String error = String.format(
                                "Error at %s. Incoming data for creation operation should not include values for temporal properties but temporal property '%s' had value %s.",
                                this.getContextString(),
                                dataTypeProperty,
                                jsonNode.getNodeType().toString().toLowerCase());
                        // TODO: Add this as a warning instead of an error, to support a strict mode
                        this.errors.add(error);
                    }
                }
            }
            finally
            {
                this.errorContextStack.pop();
                this.contextStack.pop();
            }
        }
    }

    private void checkRequiredPropertiesPresent(@Nonnull ImmutableList<DataTypeProperty> plainProperties)
    {
        for (DataTypeProperty property : plainProperties)
        {
            this.errorContextStack.push(new DataTypePropertyErrorContext(property));
            this.contextStack.push(property.getName());

            try
            {
                this.handlePlainProperty(property);
            }
            finally
            {
                this.errorContextStack.pop();
                this.contextStack.pop();
            }
        }
    }

    private void handlePlainProperty(@Nonnull DataTypeProperty property)
    {
        JsonNode jsonNode = this.incomingJson.path(property.getName());
        if (jsonNode.isMissingNode() || jsonNode.isNull())
        {
            String error = String.format(
                    "Error at %s. Expected value for required property '%s.%s: %s%s' but value was %s.",
                    this.getContextString(),
                    property.getOwningClassifier().getName(),
                    property.getName(),
                    property.getType().toString(),
                    property.isOptional() ? "?" : "",
                    jsonNode.getNodeType().toString().toLowerCase());
            this.errors.add(error);
        }
    }

    public String getContextString()
    {
        return this.errorContextStack
                .toList()
                .asReversed()
                .makeString("->");
    }

    @Nonnull
    private MapIterable<ImmutableList<Object>, JsonNode> indexIncomingJsonInstances(
            @Nonnull Iterable<JsonNode> childrenJsonNodes,
            @Nonnull AssociationEnd associationEnd,
            JsonNode parentJsonNode)
    {
        MutableOrderedMap<ImmutableList<Object>, JsonNode> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (JsonNode childJsonNode : childrenJsonNodes)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    childJsonNode,
                    associationEnd,
                    parentJsonNode);
            JsonNode duplicateJsonNode = result.put(keys, childJsonNode);
            if (duplicateJsonNode != null)
            {
                throw new AssertionError("TODO: Test an array of owned children with duplicates with the same key.");
            }
        }
        return result;
        // TODO: Change to use asUnmodifiable after EC 10.0 is released.
        // return result.asUnmodifiable();
    }
    //endregion

    //region AssociationEnds
    public void synchronizeAssociationEnds(
            @Nonnull Klass klass,
            @Nonnull Optional<AssociationEnd> pathHere,
            @Nonnull ObjectNode jsonNode)
    {
        PartitionImmutableList<AssociationEnd> forwardOwnedAssociationEnds = klass.getAssociationEnds()
                .reject(associationEnd -> pathHere.equals(Optional.of(associationEnd.getOpposite())))
                .partition(AssociationEnd::isOwned);

        for (AssociationEnd associationEnd : forwardOwnedAssociationEnds.getSelected())
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();

            JsonNode childJsonNode = jsonNode.path(associationEnd.getName());
            if (multiplicity.isToOne())
            {
                this.handleToOne(associationEnd, childJsonNode, jsonNode);
            }
            else
            {
                this.handleToMany(associationEnd, childJsonNode, jsonNode);
            }
        }

        for (AssociationEnd associationEnd : forwardOwnedAssociationEnds.getRejected())
        {
            Multiplicity multiplicity  = associationEnd.getMultiplicity();
            JsonNode     childJsonNode = jsonNode.path(associationEnd.getName());

            if (multiplicity.isToOne())
            {
                this.handleToOneOutsideProjection(associationEnd, childJsonNode, jsonNode);
            }
            else
            {
                this.handleToManyOutsideProjection(associationEnd, childJsonNode, jsonNode);
            }
        }
    }

    // TODO: Logic for create, needs to be polymorphic for update
    protected void handleVersion(
            AssociationEnd associationEnd,
            JsonNode childJsonNode)
    {
        if (childJsonNode.isNull() || childJsonNode.isMissingNode())
        {
            return;
        }

        throw new AssertionError();
    }

    protected void handleToOneOutsideProjection(
            AssociationEnd associationEnd,
            JsonNode childJsonNode,
            ObjectNode parentJsonNode)
    {
        if (associationEnd.isOwned())
        {
            throw new AssertionError(
                    "Assumption is that all owned association ends are inside projection, all unowned are outside projection");
        }

        String associationEndName = associationEnd.getName();
        this.contextStack.push(associationEndName);

        ImmutableList<Object> keys = this.getKeysFromJsonNode(
                childJsonNode,
                associationEnd,
                parentJsonNode);

        this.errorContextStack.push(new AssociationEndErrorContext(associationEnd, keys));
        try
        {
            if ((childJsonNode.isMissingNode() || childJsonNode.isNull())
                    && associationEnd.isRequired())
            {
                throw new AssertionError(associationEnd.toString());
            }

            Object childPersistentInstanceWithKey = this.findExistingChildPersistentInstance(
                    parentJsonNode,
                    childJsonNode,
                    associationEnd);
            if (childPersistentInstanceWithKey == null)
            {
                String error = String.format(
                        "Error at '%s'. Could not find existing persistent instance for association end '%s' with key %s.",
                        this.getContextString(),
                        associationEnd,
                        keys.makeString());
                this.errors.add(error);
            }
        }
        finally
        {
            this.errorContextStack.pop();
            this.contextStack.pop();
        }
    }

    protected Object findExistingChildPersistentInstance(
            @Nonnull ObjectNode parentJsonNode,
            @Nonnull JsonNode incomingChildInstance,
            @Nonnull AssociationEnd associationEnd)
    {
        /*
        if (!(this instanceof PersistentCreator) && !(this instanceof PersistentReplacer))
        {
            throw new AssertionError();
        }
        */
        ImmutableList<Object> keys = this.getKeysFromJsonNode(
                incomingChildInstance,
                associationEnd,
                parentJsonNode);
        return this.dataStore.findByKey(associationEnd.getType(), keys);
    }

    private void handleToManyOutsideProjection(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode childrenJsonNodes,
            JsonNode parentJsonNode)
    {
        // TODO: Test null where an array goes

        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                childrenJsonNodes,
                associationEnd,
                parentJsonNode);

        for (JsonNode childJsonNode : childrenJsonNodes)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    childJsonNode,
                    associationEnd,
                    parentJsonNode);

            throw new AssertionError();

            /*
            PersistentSynchronizer synchronizer = this.determineNextMode(OperationMode.REPLACE);
            synchronizer.synchronizeInTransaction(
                    associationEnd.getType(),
                    Optional.of(associationEnd),
                    (ObjectNode) childJsonNode);
            */
        }
    }

    public void synchronizeAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();

        JsonNode jsonNode = this.incomingJson.path(associationEnd.getName());

        if ((jsonNode.isMissingNode() || jsonNode.isNull()) && multiplicity.isRequired())
        {
            throw new AssertionError();
        }

        if (multiplicity.isToOne())
        {
            this.handleToOne(associationEnd, jsonNode, incomingJson);
        }
        else
        {
            this.handleToMany(associationEnd, jsonNode, incomingJson);
        }
    }

    public void handleToOne(
            @Nonnull AssociationEnd associationEnd,
            JsonNode childJsonNode,
            JsonNode parentJsonNode)
    {
        String associationEndName = associationEnd.getName();
        this.contextStack.push(associationEndName);

        ImmutableList<Object> keys = this.getKeysFromJsonNode(
                childJsonNode,
                associationEnd,
                parentJsonNode);

        this.errorContextStack.push(new AssociationEndErrorContext(associationEnd, keys));
        try
        {
            ObjectNode childObjectNode = (ObjectNode) childJsonNode;
            IncomingCreateDataModelValidator validator = new IncomingCreateDataModelValidator(
                    this.dataStore,
                    associationEnd.getType(),
                    childObjectNode,
                    this.errors,
                    this.errorContextStack,
                    this.contextStack,
                    Optional.of(associationEnd),
                    false);
            validator.synchronize();
        }
        finally
        {
            this.errorContextStack.pop();
            this.contextStack.pop();
        }
    }

    public void handleToMany(
            @Nonnull AssociationEnd associationEnd,
            JsonNode incomingChildInstances,
            JsonNode parentJsonNode)
    {
        ImmutableList<JsonNode> incomingInstancesForInsert = this.filterIncomingInstancesForInsert(
                incomingChildInstances,
                associationEnd);

        // TODO: Figure out how to recurse without checking key
        ImmutableList<JsonNode> incomingInstancesForUpdate = this.filterIncomingInstancesForUpdate(
                incomingChildInstances,
                associationEnd);

        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                incomingInstancesForUpdate,
                associationEnd,
                parentJsonNode);

        for (int index = 0; index < incomingChildInstances.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    associationEnd.getName(),
                    index);

            JsonNode childJsonNode = incomingChildInstances.get(index);
            ImmutableList<Object> keysFromJsonNode = this.getKeysFromJsonNode(
                    childJsonNode,
                    associationEnd,
                    parentJsonNode);

            AssociationEndWithIndexErrorContext associationEndWithIndex = new AssociationEndWithIndexErrorContext(
                    associationEnd,
                    index,
                    keysFromJsonNode);
            this.errorContextStack.push(associationEndWithIndex);
            this.contextStack.push(contextString);

            try
            {
                /*
                if (this.jsonNodeNeedsIdInferredOnInsert(childJsonNode, associationEnd))
                {
                    continue;
                }
                */

                ObjectNode childObjectNode = (ObjectNode) childJsonNode;
                IncomingCreateDataModelValidator validator = new IncomingCreateDataModelValidator(
                        this.dataStore,
                        associationEnd.getType(),
                        childObjectNode,
                        this.errors,
                        this.errorContextStack,
                        this.contextStack,
                        Optional.of(associationEnd),
                        false);
                validator.synchronize();

                /*
                if (childPersistentInstance == null)
                {
                    // TODO: Implement CreateUpdateDataModelValidator and recurse in create mode
                    return;
                }
                if (childPersistentInstance != null && childJsonNode instanceof ObjectNode)
                {
                    this.synchronizeAssociationEnd(
                            associationEnd,
                            (ObjectNode) childJsonNode,
                            (ObjectNode) childPersistentInstance);
                }
                */
            }
            finally
            {
                this.errorContextStack.pop();
                this.contextStack.pop();
            }
        }
    }

    private ImmutableList<JsonNode> filterIncomingInstancesForUpdate(
            JsonNode incomingChildInstances, AssociationEnd associationEnd)
    {
        return Lists.immutable.withAll(incomingChildInstances)
                .rejectWith(
                        this::jsonNodeNeedsIdInferredOnInsert,
                        associationEnd);
    }

    private ImmutableList<JsonNode> filterIncomingInstancesForInsert(
            JsonNode incomingChildInstances,
            AssociationEnd associationEnd)
    {
        return Lists.immutable.withAll(incomingChildInstances)
                .selectWith(
                        this::jsonNodeNeedsIdInferredOnInsert,
                        associationEnd);
    }

    private boolean needsTermination(
            Object persistentChildInstance,
            @Nonnull Klass klass,
            @Nonnull MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey)
    {
        ImmutableList<Object> keys = this.getKeysFromPersistentInstance(persistentChildInstance, klass);
        return !incomingChildInstancesByKey.containsKey(keys);
    }

    protected ImmutableList<Object> getKeysFromPersistentInstance(Object persistentInstance, @Nonnull Klass klass)
    {
        return klass
                .getKeyProperties()
                .collect(keyProperty -> this.dataStore.getDataTypeProperty(persistentInstance, keyProperty));
    }

    public void synchronizeAssociationEnd(
            @Nonnull AssociationEnd associationEnd,
            ObjectNode parentJsonNode,
            ObjectNode childJsonNode)
    {
        // TODO: Figure out a mode here, based on whether the associated type has auto-generated, but present keys.

        throw new AssertionError();
    }
    //endregion

    private Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

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

    private Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            JsonNode parentJsonNode)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.get(opposite);

        if (oppositeForeignKeys.notEmpty())
        {
            DataTypeProperty oppositeForeignKey     = oppositeForeignKeys.getOnly();
            String           oppositeForeignKeyName = oppositeForeignKey.getName();
            Object           result                 = parentJsonNode.get(oppositeForeignKeyName);
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
            @Nonnull Klass klass)
    {
        return klass
                .getKeyProperties()
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode));
    }

    private ImmutableList<Object> getKeysFromJsonNode(
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            JsonNode parentJsonNode)
    {
        Klass                           type                    = associationEnd.getType();
        ImmutableList<DataTypeProperty> keyProperties           = type.getKeyProperties();
        ImmutableList<DataTypeProperty> nonForeignKeyProperties = keyProperties.reject(DataTypeProperty::isForeignKey);
        return nonForeignKeyProperties
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode,
                        associationEnd,
                        parentJsonNode));
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
