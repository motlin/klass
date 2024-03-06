package cool.klass.reladomo.persistent.writer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.JsonDataTypeValueVisitor;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class IncomingUpdateDataModelValidator
{
    private final DataStore            dataStore;
    private final Klass                klass;
    private final Object               persistentInstance;
    private final ObjectNode           objectNode;
    private final MutableList<String>  errors;
    private final MutableStack<String> contextStack;
    private final boolean              isRoot;

    public IncomingUpdateDataModelValidator(
            DataStore dataStore,
            Klass klass,
            Object persistentInstance,
            ObjectNode objectNode,
            MutableList<String> errors,
            MutableStack<String> contextStack,
            boolean isRoot)
    {
        this.dataStore = Objects.requireNonNull(dataStore);
        this.klass = Objects.requireNonNull(klass);
        this.persistentInstance = Objects.requireNonNull(persistentInstance);
        this.objectNode = Objects.requireNonNull(objectNode);
        this.errors = Objects.requireNonNull(errors);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.isRoot = isRoot;
    }

    public static void validate(
            DataStore dataStore,
            Klass klass,
            Object persistentInstance,
            ObjectNode objectNode,
            MutableList<String> errors)
    {
        IncomingUpdateDataModelValidator validator = new IncomingUpdateDataModelValidator(
                dataStore,
                klass,
                persistentInstance,
                objectNode,
                errors,
                Stacks.mutable.empty(),
                true);
        validator.validate();
    }

    public void validate()
    {
        if (this.isRoot)
        {
            this.contextStack.push(this.klass.getName());
        }
        try
        {
            this.handleDataTypeProperties();
            this.klass.getAssociationEnds()
                    .select(AssociationEnd::isOwned)
                    .each(this::handleAssociationEnd);
        }
        finally
        {
            if (this.isRoot)
            {
                this.contextStack.pop();
            }
        }
    }

    public void handleAssociationEnd(AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();

        JsonNode jsonNode = this.objectNode.path(associationEnd.getName());

        if ((jsonNode.isMissingNode() || jsonNode.isNull()) && multiplicity.isRequired())
        {
            throw new AssertionError();
        }

        if (multiplicity.isToOne())
        {
            this.handleToOne(associationEnd, jsonNode);
        }
        else
        {
            this.handleToMany(associationEnd, jsonNode);
        }
    }

    public void handleToOne(
            AssociationEnd associationEnd,
            JsonNode jsonNode)
    {
        this.contextStack.push(associationEnd.getName());
        try
        {
            if (jsonNode instanceof ObjectNode)
            {
                Object childPersistentInstance = this.dataStore.getToOne(
                        this.persistentInstance,
                        associationEnd);
                this.handleAssociationEnd(associationEnd, (ObjectNode) jsonNode, childPersistentInstance);
            }
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    public void handleToMany(
            AssociationEnd associationEnd,
            JsonNode incomingChildInstances)
    {
        if (!(incomingChildInstances instanceof ArrayNode))
        {
            this.emitNonArrayError(associationEnd, incomingChildInstances);
            return;
        }

        ImmutableList<JsonNode> incomingInstancesForInsert = this.filterIncomingInstancesForInsert(
                incomingChildInstances,
                associationEnd);

        // TODO: Figure out how to recurse without checking key
        ImmutableList<JsonNode> incomingInstancesForUpdate = this.filterIncomingInstancesForUpdate(
                incomingChildInstances,
                associationEnd);

        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                incomingInstancesForUpdate,
                associationEnd);

        MapIterable<ImmutableList<Object>, Object> persistentChildInstancesByKey = this.getPersistentChildInstancesByKey(
                incomingChildInstancesByKey,
                associationEnd);

        for (int index = 0; index < incomingChildInstances.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    associationEnd.getName(),
                    index);
            this.contextStack.push(contextString);

            try
            {
                JsonNode childJsonNode = incomingChildInstances.path(index);
                if (this.jsonNodeNeedsIdInferredOnInsert(childJsonNode, associationEnd))
                {
                    continue;
                }

                ImmutableList<Object> keysFromJsonNode        = this.getKeysFromJsonNode(childJsonNode, associationEnd);
                Object                childPersistentInstance = persistentChildInstancesByKey.get(keysFromJsonNode);
                /*
                if (childPersistentInstance == null)
                {
                    // TODO: Implement CreateUpdateDataModelValidator and recurse in create mode
                    return;
                }
                */
                if (childPersistentInstance != null && childJsonNode instanceof ObjectNode)
                {
                    this.handleAssociationEnd(associationEnd, (ObjectNode) childJsonNode, childPersistentInstance);
                }
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    private MapIterable<ImmutableList<Object>, Object> getPersistentChildInstancesByKey(
            MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey, AssociationEnd associationEnd)
    {
        List<Object> persistentChildInstances = this.dataStore.getToMany(this.persistentInstance, associationEnd);
        MutableList<Object> nonTerminatedPersistentChildInstances = ListAdapter.adapt(persistentChildInstances)
                .reject(persistentChildInstance -> this.needsTermination(
                        persistentChildInstance,
                        associationEnd.getType(),
                        incomingChildInstancesByKey));
        return this.indexPersistentInstances(
                nonTerminatedPersistentChildInstances,
                associationEnd.getType());
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

    private void emitNonArrayError(AssociationEnd associationEnd, JsonNode incomingChildInstances)
    {
        this.contextStack.push(associationEnd.getName());
        String error = String.format(
                "Error at %s. Expected json array but value was %s.",
                this.getContextString(),
                incomingChildInstances.getNodeType().toString().toLowerCase());
        this.errors.add(error);
        this.contextStack.pop();
    }

    private MapIterable<ImmutableList<Object>, Object> indexPersistentInstances(
            List<Object> persistentInstances,
            Klass klass)
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

    private boolean needsTermination(
            Object persistentChildInstance,
            Klass klass,
            MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey)
    {
        ImmutableList<Object> keys = this.getKeysFromPersistentInstance(persistentChildInstance, klass);
        return !incomingChildInstancesByKey.containsKey(keys);
    }

    protected ImmutableList<Object> getKeysFromPersistentInstance(Object persistentInstance, Klass klass)
    {
        return klass
                .getKeyProperties()
                .collect(keyProperty -> this.dataStore.getDataTypeProperty(persistentInstance, keyProperty));
    }

    public void handleAssociationEnd(
            AssociationEnd associationEnd,
            ObjectNode objectNode,
            Object persistentInstance)
    {
        IncomingUpdateDataModelValidator validator = new IncomingUpdateDataModelValidator(
                this.dataStore,
                associationEnd.getType(),
                persistentInstance,
                objectNode,
                this.errors,
                this.contextStack,
                false);
        validator.validate();
    }

    private void handleDataTypeProperties()
    {
        ImmutableList<DataTypeProperty> plainProperties = this.klass.getDataTypeProperties()
                .reject(DataTypeProperty::isID)
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isTemporal)
                .reject(DataTypeProperty::isAudit)
                .reject(DataTypeProperty::isForeignKey);

        this.handleIdProperties(this.klass.getDataTypeProperties().select(DataTypeProperty::isID));
        this.checkPresentPropertiesMatch(this.klass.getDataTypeProperties().select(DataTypeProperty::isTemporal));
        this.checkPresentPropertiesMatch(this.klass.getDataTypeProperties().select(DataTypeProperty::isAudit));
        this.checkRequiredPropertiesPresent(plainProperties);
    }

    private void handleIdProperties(ImmutableList<DataTypeProperty> idProperties)
    {
        this.checkPresentPropertiesMatch(idProperties);
        if (!this.isRoot)
        {
            this.checkRequiredPropertiesPresent(idProperties);
        }
    }

    private void checkPresentPropertiesMatch(ImmutableList<DataTypeProperty> properties)
    {
        for (DataTypeProperty dataTypeProperty : properties)
        {
            this.contextStack.push(dataTypeProperty.getName());

            try
            {
                JsonNode jsonNode = this.objectNode.path(dataTypeProperty.getName());
                if (!jsonNode.isMissingNode() && !jsonNode.isNull())
                {
                    this.checkPresentPropertyMatches(dataTypeProperty);
                }
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    private void checkPresentPropertyMatches(DataTypeProperty property)
    {
        Object persistentValue = this.dataStore.getDataTypeProperty(this.persistentInstance, property);
        Object incomingValue   = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(property, this.objectNode);
        if (!persistentValue.equals(incomingValue))
        {
            // TODO: Clarify that the value is allowed to be absent, but if present, it must match?
            String error = String.format(
                    "Error at %s. Mismatched value for property '%s.%s: %s%s'. Expected absent value or %s but value was %s.",
                    this.getContextString(),
                    property.getOwningClassifier().getName(),
                    property.getName(),
                    property.getType().toString(),
                    property.isOptional() ? "?" : "",
                    persistentValue,
                    incomingValue);
            this.errors.add(error);
        }
    }

    private void checkRequiredPropertiesPresent(ImmutableList<DataTypeProperty> plainProperties)
    {
        for (DataTypeProperty property : plainProperties)
        {
            this.contextStack.push(property.getName());

            try
            {
                this.handlePlainProperty(property);
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    private void handlePlainProperty(DataTypeProperty property)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
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
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }

    private MapIterable<ImmutableList<Object>, JsonNode> indexIncomingJsonInstances(
            Iterable<JsonNode> incomingInstances,
            AssociationEnd associationEnd)
    {
        MutableOrderedMap<ImmutableList<Object>, JsonNode> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (JsonNode incomingInstance : incomingInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingInstance,
                    associationEnd);
            JsonNode duplicateJsonNode = result.put(keys, incomingInstance);
            if (duplicateJsonNode != null)
            {
                throw new AssertionError("TODO: Test an array of owned children with duplicates with the same key.");
            }
        }
        return result;
        // TODO: Change to use asUnmodifiable after EC 10.0 is released.
        // return result.asUnmodifiable();
    }

    private boolean jsonNodeNeedsIdInferredOnInsert(
            JsonNode jsonNode,
            AssociationEnd associationEnd)
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
            JsonNode jsonNode,
            AssociationEnd associationEnd)
    {
        return associationEnd
                .getType()
                .getKeyProperties()
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode,
                        associationEnd));
    }

    private boolean jsonNodeNeedsIdInferredOnInsert(
            DataTypeProperty keyProperty,
            JsonNode jsonNode,
            AssociationEnd associationEnd)
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

    private Object getKeyFromJsonNode(
            DataTypeProperty keyProperty,
            JsonNode jsonNode,
            AssociationEnd associationEnd)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.get(opposite);

        if (oppositeForeignKeys.notEmpty())
        {
            Object result = this.dataStore.getDataTypeProperty(
                    this.persistentInstance,
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
}
