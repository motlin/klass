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
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;
import org.eclipse.collections.impl.map.mutable.MapAdapter;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class IncomingCreateDataModelValidator
{
    @Nonnull
    protected final DataStore                dataStore;
    @Nonnull
    protected final Klass                    klass;
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

    public IncomingCreateDataModelValidator(
            @Nonnull DataStore dataStore,
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        this.dataStore    = Objects.requireNonNull(dataStore);
        this.klass        = Objects.requireNonNull(klass);
        this.objectNode   = Objects.requireNonNull(objectNode);
        this.errors       = Objects.requireNonNull(errors);
        this.warnings     = Objects.requireNonNull(warnings);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.pathHere     = Objects.requireNonNull(pathHere);
        this.isRoot       = isRoot;
    }

    public static void validate(
            @Nonnull DataStore dataStore,
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings)
    {
        IncomingCreateDataModelValidator validator = new IncomingCreateDataModelValidator(
                dataStore,
                klass,
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
        if (dataTypeProperty.isID())
        {
            // TODO: inside the projection, only check for matching, and only in update mode
            // this.handleIdProperty(dataTypeProperty);
        }
        else if (dataTypeProperty.isKey())
        {
            // TODO: inside the projection, only check for matching, and only in update mode
            // this.handleKeyProperty(dataTypeProperty);
        }
        else if (!dataTypeProperty.isDerived()
                && !dataTypeProperty.isTemporal()
                && !dataTypeProperty.isAudit()
                && dataTypeProperty.isForeignKey())
        {
            // throw new AssertionError(dataTypeProperty);
        }
        else if (dataTypeProperty.isVersion())
        {
            throw new AssertionError();
        }
    }

    private void handlePlainProperty(@Nonnull DataTypeProperty property)
    {
        if (!property.isRequired())
        {
            return;
        }

        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode() || jsonNode.isNull())
        {
            String error = String.format(
                    "Error at %s. Expected value for required property '%s.%s: %s%s' but value was %s.",
                    this.getContextString(),
                    property.getOwningClassifier().getName(),
                    property.getName(),
                    property.getType(),
                    property.isOptional() ? "?" : "",
                    jsonNode.getNodeType().toString().toLowerCase());
            this.errors.add(error);
        }
    }

    private void checkPresentPropertiesMatch(@Nonnull ImmutableList<DataTypeProperty> properties)
    {
        for (DataTypeProperty dataTypeProperty : properties)
        {
            this.contextStack.push(dataTypeProperty.getName());

            try
            {
                JsonNode jsonNode = this.objectNode.path(dataTypeProperty.getName());
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
                this.contextStack.pop();
            }
        }
    }

    private void checkRequiredPropertiesPresent(@Nonnull ImmutableList<DataTypeProperty> plainProperties)
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
            return;
        }

        if (associationEnd.isOwned())
        {
            this.handleOwnedAssociationEnd(associationEnd);
        }
        else
        {
            this.handleOutsideProjectionReferenceProperty(associationEnd);
        }
    }

    private void handleOutsideProjectionReferenceProperty(@Nonnull AssociationEnd associationEnd)
    {
        Multiplicity multiplicity  = associationEnd.getMultiplicity();
        JsonNode     childJsonNode = this.objectNode.path(associationEnd.getName());

        if (multiplicity.isToOne())
        {
            this.handleToOneOutsideProjection(associationEnd, childJsonNode, this.objectNode);
        }
        else
        {
            this.handleToManyOutsideProjection(associationEnd, childJsonNode, this.objectNode);
        }
    }

    private void handleOwnedAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();

        if (multiplicity.isToOne())
        {
            this.handleOwnedToOne(associationEnd);
        }
        else
        {
            this.handleOwnedToMany(associationEnd);
        }
    }

    private boolean isBackward(@Nonnull AssociationEnd associationEnd)
    {
        return this.pathHere.equals(Optional.of(associationEnd.getOpposite()));
    }

    protected void handleToOneOutsideProjection(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode childJsonNode,
            @Nonnull ObjectNode parentJsonNode)
    {
        if (associationEnd.isOwned())
        {
            throw new AssertionError("Assumption is that all owned association ends are inside projection, all unowned are outside projection");
        }

        if (childJsonNode.isMissingNode()
                || childJsonNode.isNull())
        {
            return;
        }

        if (!associationEnd.hasRealKeys())
        {
            return;
        }

        String associationEndName = associationEnd.getName();
        this.contextStack.push(associationEndName);

        try
        {
            if ((childJsonNode.isMissingNode() || childJsonNode.isNull())
                    && associationEnd.isRequired())
            {
                // TODO: Check for non-private foreign key properties
                // TODO: Can we remove this?
                return;
            }

            if (childJsonNode.isMissingNode() && !associationEnd.isRequired())
            {
                // TODO: Can we remove this?
                return;
            }

            MapIterable<DataTypeProperty, Object> keys = this.getKeysFromJsonNode(
                    childJsonNode,
                    associationEnd,
                    parentJsonNode);
            if (keys.contains(null))
            {
                return;
            }

            Object childPersistentInstanceWithKey = this.findExistingChildPersistentInstance(associationEnd, keys);
            if (childPersistentInstanceWithKey == null)
            {
                String keysString = keys
                        .keyValuesView()
                        .collect(keyValue -> keyValue.getOne().getName() + ": " + keyValue.getTwo())
                        .makeString("{", ", ", "}");

                String error = String.format(
                        "Error at '%s'. Could not find existing persistent instance for association end '%s' with key %s.",
                        this.getContextString(),
                        associationEnd,
                        keysString);

                this.errors.add(error);
            }
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    protected Object findExistingChildPersistentInstance(
            @Nonnull AssociationEnd associationEnd,
            MapIterable<DataTypeProperty, Object> keys)
    {
        /*
        if (!(this instanceof PersistentCreator) && !(this instanceof PersistentReplacer))
        {
            throw new AssertionError();
        }
        */

        return this.dataStore.findByKey(associationEnd.getType(), keys);
    }

    private void handleToManyOutsideProjection(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode childrenJsonNodes,
            @Nonnull JsonNode parentJsonNode)
    {
        for (JsonNode childJsonNode : childrenJsonNodes)
        {
            MapIterable<DataTypeProperty, Object> keys = this.getKeysFromJsonNode(
                    childJsonNode,
                    associationEnd,
                    parentJsonNode);

            throw new AssertionError(associationEnd);
        }
    }

    public void handleOwnedToOne(@Nonnull AssociationEnd associationEnd)
    {
        JsonNode childJsonNode = this.objectNode.path(associationEnd.getName());

        String associationEndName = associationEnd.getName();
        this.contextStack.push(associationEndName);

        MapIterable<DataTypeProperty, Object> keys = this.getKeysFromJsonNode(
                childJsonNode,
                associationEnd,
                this.objectNode);

        try
        {
            if (!(childJsonNode instanceof ObjectNode))
            {
                String error = String.format(
                        "Error at '%s'. Expected JSON object for owned association end '%s' but got %s.",
                        this.getContextString(),
                        associationEnd,
                        childJsonNode.getNodeType().toString().toLowerCase());
                this.errors.add(error);
            }
            ObjectNode childObjectNode = (ObjectNode) childJsonNode;
            IncomingCreateDataModelValidator validator = new IncomingCreateDataModelValidator(
                    this.dataStore,
                    associationEnd.getType(),
                    childObjectNode,
                    this.errors,
                    this.warnings,
                    this.contextStack,
                    Optional.of(associationEnd),
                    false);
            validator.validate();
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    public void handleOwnedToMany(@Nonnull AssociationEnd associationEnd)
    {
        JsonNode incomingChildInstances = this.objectNode.path(associationEnd.getName());

        // TODO: Figure out how to recurse without checking key
        ImmutableList<JsonNode> incomingInstancesForInsert = this.filterIncomingInstancesForInsert(
                incomingChildInstances,
                associationEnd);

        ImmutableList<JsonNode> incomingInstancesForUpdate = this.filterIncomingInstancesForUpdate(
                incomingChildInstances,
                associationEnd);

        /*
        MapIterable<ImmutableList<Object>, JsonNode> incomingChildInstancesByKey = this.indexIncomingJsonInstances(
                incomingInstances,
                associationEnd,
                this.objectNode);
        */

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
                        this.warnings,
                        this.contextStack,
                        Optional.of(associationEnd),
                        false);
                validator.validate();

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

    //endregion

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

    private MapIterable<DataTypeProperty, Object> getKeysFromJsonNode(
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode parentJsonNode)
    {
        MutableMap<DataTypeProperty, Object> result = MapAdapter.adapt(new LinkedHashMap<>());

        Klass                           type                    = associationEnd.getType();
        ImmutableList<DataTypeProperty> keyProperties           = type.getKeyProperties();
        ImmutableList<DataTypeProperty> nonForeignKeyProperties = keyProperties.reject(DataTypeProperty::isForeignKey);
        for (DataTypeProperty keyProperty : nonForeignKeyProperties)
        {
            result.put(
                    keyProperty,
                    this.getKeyFromJsonNode(
                            keyProperty,
                            jsonNode,
                            associationEnd,
                            parentJsonNode));
        }
        return result.toImmutable();
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
            DataTypeProperty oppositeForeignKey     = oppositeForeignKeys.getOnly();
            String           oppositeForeignKeyName = oppositeForeignKey.getName();
            Object           result                 = parentJsonNode.path(oppositeForeignKeyName);
            return Objects.requireNonNull(result);
        }

        if (keysMatchingThisForeignKey.notEmpty())
        {
            if (keysMatchingThisForeignKey.size() != 1)
            {
                throw new AssertionError();
            }

            Pair<AssociationEnd, ImmutableList<DataTypeProperty>> pair = keysMatchingThisForeignKey.keyValuesView().getOnly();

            JsonNode childNode = jsonNode.path(pair.getOne().getName());
            Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                    pair.getTwo().getOnly(),
                    (ObjectNode) childNode);
            return Objects.requireNonNull(result);
        }

        if (jsonNode instanceof ObjectNode)
        {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                    keyProperty,
                    objectNode);

            if (result == null)
            {
                String error = String.format(
                        "Error at %s. Expected value for key property '%s.%s: %s%s' but value was %s.",
                        this.getContextString(),
                        keyProperty.getOwningClassifier().getName(),
                        keyProperty.getName(),
                        keyProperty.getType(),
                        keyProperty.isOptional() ? "?" : "",
                        result);
                this.errors.add(error);
            }
            return result;
        }

        throw new AssertionError(jsonNode);
    }
}
