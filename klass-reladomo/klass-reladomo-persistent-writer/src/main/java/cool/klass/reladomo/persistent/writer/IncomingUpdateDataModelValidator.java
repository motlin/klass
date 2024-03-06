package cool.klass.reladomo.persistent.writer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.AssertValuesMatchPropertyVisitor;
import cool.klass.deserializer.json.JsonDataTypeValueVisitor;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class IncomingUpdateDataModelValidator
{
    @Nonnull
    protected final DataStore                dataStore;
    @Nonnull
    protected final Klass                    klass;
    protected final Object                   persistentInstance;
    @Nonnull
    protected final ObjectNode               objectNode;
    @Nonnull
    protected final MutableList<String>      errors;
    @Nonnull
    protected final MutableList<String>      warnings;
    @Nonnull
    protected final MutableStack<String>     contextStack;
    @Nonnull
    protected final Optional<AssociationEnd> pathHere;
    protected final boolean                  isRoot;

    public IncomingUpdateDataModelValidator(
            @Nonnull DataStore dataStore,
            @Nonnull Klass klass,
            @Nonnull Object persistentInstance,
            @Nonnull ObjectNode objectNode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        this.dataStore          = Objects.requireNonNull(dataStore);
        this.klass              = Objects.requireNonNull(klass);
        this.persistentInstance = Objects.requireNonNull(persistentInstance);
        this.objectNode         = Objects.requireNonNull(objectNode);
        this.errors             = Objects.requireNonNull(errors);
        this.warnings           = Objects.requireNonNull(warnings);
        this.contextStack       = Objects.requireNonNull(contextStack);
        this.pathHere           = Objects.requireNonNull(pathHere);
        this.isRoot             = isRoot;
    }

    public static void validate(
            @Nonnull DataStore dataStore,
            @Nonnull Klass klass,
            @Nonnull Object persistentInstance,
            @Nonnull ObjectNode objectNode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings)
    {
        IncomingUpdateDataModelValidator validator = new IncomingUpdateDataModelValidator(
                dataStore,
                klass,
                persistentInstance,
                objectNode,
                errors,
                warnings,
                Stacks.mutable.empty(),
                Optional.empty(),
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
            this.handleDataTypePropertiesInsideProjection();
            this.handleAssociationEnds();
        }
        finally
        {
            if (this.isRoot)
            {
                this.contextStack.pop();
            }
        }
    }

    //region DataTypeProperties
    private void handleDataTypePropertiesInsideProjection()
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = this.klass.getDataTypeProperties();
        for (DataTypeProperty dataTypeProperty : dataTypeProperties)
        {
            this.handleDataTypePropertyInsideProjection(dataTypeProperty);
        }
    }

    private void handleDataTypePropertyInsideProjection(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isTemporal())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "temporal");
            return;
        }
        if (dataTypeProperty.isAudit())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "audit");
            return;
        }
        if (dataTypeProperty.isVersion())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "version");
            return;
        }
        if (dataTypeProperty.isFinal())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "final");
        }
    }

    private void checkPropertyMatchesIfPresent(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull String propertyKind)
    {
        this.contextStack.push(dataTypeProperty.getName());

        try
        {
            this.checkPropertyMatches(dataTypeProperty, propertyKind);
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    private void checkPropertyMatches(@Nonnull DataTypeProperty property, @Nonnull String propertyKind)
    {
        JsonNode jsonDataTypeValue = this.objectNode.path(property.getName());
        if (jsonDataTypeValue.isMissingNode())
        {
            return;
        }

        Object persistentValue = this.dataStore.getDataTypeProperty(this.persistentInstance, property);
        PropertyVisitor visitor = new AssertValuesMatchPropertyVisitor(
                jsonDataTypeValue,
                persistentValue,
                propertyKind,
                this.contextStack,
                this.errors);
        property.visit(visitor);
    }

    public String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }
    //endregion

    //region AssociationEnds
    private void handleAssociationEnds()
    {
        for (AssociationEnd associationEnd : this.klass.getAssociationEnds())
        {
            this.handleAssociationEnd(associationEnd);
        }
    }

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        if (this.isBackward(associationEnd))
        {
            return;
        }

        if (associationEnd.isVersion())
        {
            this.handleVersionAssociationEnd(associationEnd);
            return;
        }

        Multiplicity multiplicity = associationEnd.getMultiplicity();

        JsonNode jsonNode = this.objectNode.path(associationEnd.getName());

        if (jsonNode.isMissingNode() || jsonNode.isNull())
        {
            return;
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

    private void handleAssociationEnd(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ObjectNode objectNode,
            @Nonnull Object persistentInstance)
    {
        IncomingUpdateDataModelValidator validator = new IncomingUpdateDataModelValidator(
                this.dataStore,
                associationEnd.getType(),
                persistentInstance,
                objectNode,
                this.errors,
                this.warnings,
                this.contextStack,
                // Yeah?
                Optional.of(associationEnd),
                false);
        validator.validate();
    }

    private void handleVersionAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        this.contextStack.push(associationEnd.getName());
        try
        {
            JsonNode jsonNode = this.objectNode.path(associationEnd.getName());

            if (this.persistentInstance == null)
            {
                this.handleWarnIfPresent(associationEnd, "version");
                return;
            }

            if (jsonNode.isMissingNode() || jsonNode.isNull())
            {
                if (this.klass.getKeyProperties().noneSatisfy(DataTypeProperty::isID))
                {
                    this.handleErrorIfAbsent(associationEnd, "version");
                }
                return;
            }

            Object childPersistentInstance = this.dataStore.getToOne(
                    this.persistentInstance,
                    associationEnd);
            if (!(jsonNode instanceof ObjectNode))
            {
                return;
            }

            ObjectNode objectNode = (ObjectNode) jsonNode;
            this.handleAssociationEnd(associationEnd, objectNode, childPersistentInstance);
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    private void handleErrorIfAbsent(@Nonnull AssociationEnd associationEnd, String propertyKind)
    {
        JsonNode jsonNode = this.objectNode.path(associationEnd.getName());
        if (!jsonNode.isMissingNode() && !jsonNode.isNull())
        {
            return;
        }

        String error = String.format(
                "Error at %s. Expected value for %s property '%s.%s: %s[%s]' but value was %s.",
                this.getContextString(),
                propertyKind,
                associationEnd.getOwningClassifier().getName(),
                associationEnd.getName(),
                associationEnd.getType(),
                associationEnd.getMultiplicity().getPrettyName(),
                jsonNode.getNodeType().toString().toLowerCase());
        this.errors.add(error);
    }

    private void handleWarnIfPresent(@Nonnull AssociationEnd property, String propertyKind)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode())
        {
            return;
        }

        if (jsonNode.isNull())
        {
            String warning = String.format(
                    "Warning at %s. Didn't expect to receive value for %s association end '%s.%s: %s[%s]' but value was null.",
                    this.getContextString(),
                    propertyKind,
                    property.getOwningClassifier().getName(),
                    property.getName(),
                    property.getType(),
                    property.getMultiplicity().getPrettyName());
            this.warnings.add(warning);
            return;
        }

        String warning = String.format(
                "Warning at %s. Didn't expect to receive value for %s association end '%s.%s: %s[%s]' but value was %s: %s.",
                this.getContextString(),
                propertyKind,
                property.getOwningClassifier().getName(),
                property.getName(),
                property.getType(),
                property.getMultiplicity().getPrettyName(),
                jsonNode.getNodeType().toString().toLowerCase(),
                jsonNode);
        this.warnings.add(warning);
    }

    private boolean isBackward(@Nonnull AssociationEnd associationEnd)
    {
        return this.pathHere.equals(Optional.of(associationEnd.getOpposite()));
    }

    public void handleToOne(
            @Nonnull AssociationEnd associationEnd,
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
            @Nonnull AssociationEnd associationEnd,
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

        MapIterable<ImmutableList<Object>, Object> persistentChildInstancesByKey =
                this.getPersistentChildInstancesByKey(
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

                ImmutableList<Object> keysFromJsonNode        = this.getKeysFromJsonNode(childJsonNode, associationEnd, this.objectNode);
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

    @Nonnull
    private MapIterable<ImmutableList<Object>, Object> getPersistentChildInstancesByKey(
            @Nonnull MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey,
            @Nonnull AssociationEnd associationEnd)
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

    private void emitNonArrayError(@Nonnull AssociationEnd associationEnd, @Nonnull JsonNode incomingChildInstances)
    {
        this.contextStack.push(associationEnd.getName());
        String error = String.format(
                "Error at %s. Expected json array but value was %s.",
                this.getContextString(),
                incomingChildInstances.getNodeType().toString().toLowerCase());
        this.errors.add(error);
        this.contextStack.pop();
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

    @Nonnull
    private MapIterable<ImmutableList<Object>, JsonNode> indexIncomingJsonInstances(
            @Nonnull Iterable<JsonNode> incomingInstances,
            @Nonnull AssociationEnd associationEnd)
    {
        MutableOrderedMap<ImmutableList<Object>, JsonNode> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (JsonNode incomingInstance : incomingInstances)
        {
            ImmutableList<Object> keys = this.getKeysFromJsonNode(
                    incomingInstance,
                    associationEnd,
                    this.objectNode);
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

    private boolean jsonNodeNeedsIdInferredOnInsert(
            @Nonnull DataTypeProperty keyProperty,
            JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd)
    {
        OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey =
                keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.getIfAbsentValue(opposite, Lists.immutable.empty());

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

    private ImmutableList<Object> getKeysFromJsonNode(
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode parentJsonNode)
    {
        Klass type = associationEnd.getType();
        ImmutableList<DataTypeProperty> keyProperties = type.getKeyProperties();
        return keyProperties
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode,
                        associationEnd,
                        parentJsonNode));
    }

    private Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode parentJsonNode)
    {
        OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey =
                keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.getIfAbsentValue(opposite, Lists.immutable.empty());

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

            Pair<AssociationEnd, ImmutableList<DataTypeProperty>> pair = keysMatchingThisForeignKey.keyValuesView().getOnly();

            AssociationEnd foreignKeyAssociation     = pair.getOne();
            String         foreignKeyAssociationName = foreignKeyAssociation.getName();
            JsonNode childNode = jsonNode.path(foreignKeyAssociationName);
            if (childNode instanceof ObjectNode)
            {
                Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                        pair.getTwo().getOnly(),
                        (ObjectNode) childNode);
                return Objects.requireNonNull(result);
            }
            if (childNode.isMissingNode())
            {
                Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                        keyProperty,
                        (ObjectNode) jsonNode);
                return Objects.requireNonNull(result);
            }
            throw new AssertionError(childNode.getClass().getCanonicalName());
        }

        if (jsonNode instanceof ObjectNode)
        {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                    keyProperty,
                    objectNode);
            return Objects.requireNonNull(result);
        }

        throw new AssertionError();
    }
}
