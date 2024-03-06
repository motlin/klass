package cool.klass.deserializer.json;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Stacks;

public class RequiredPropertiesValidator
{
    @Nonnull
    private final Klass                    klass;
    @Nonnull
    private final ObjectNode               objectNode;
    @Nonnull
    private final OperationMode            operationMode;
    @Nonnull
    private final MutableList<String>      errors;
    @Nonnull
    private final MutableList<String>      warnings;
    @Nonnull
    private final MutableStack<String>     contextStack;
    @Nonnull
    private final Optional<AssociationEnd> pathHere;
    private final boolean                  isRoot;

    public RequiredPropertiesValidator(
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        this.klass = Objects.requireNonNull(klass);
        this.objectNode = Objects.requireNonNull(objectNode);
        this.operationMode = Objects.requireNonNull(operationMode);
        this.errors = Objects.requireNonNull(errors);
        this.warnings = Objects.requireNonNull(warnings);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.pathHere = Objects.requireNonNull(pathHere);
        this.isRoot = isRoot;
    }

    public static void validate(
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings)
    {
        RequiredPropertiesValidator validator = new RequiredPropertiesValidator(
                klass,
                objectNode,
                operationMode,
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
            this.handleDataTypeProperties();
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
    private void handleDataTypeProperties()
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = this.klass.getDataTypeProperties();
        for (DataTypeProperty dataTypeProperty : dataTypeProperties)
        {
            this.handleDataTypeProperty(dataTypeProperty);
        }
    }

    private void handleDataTypeProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isID())
        {
            this.handleIdProperty(dataTypeProperty);
        }
        else if (dataTypeProperty.isKey())
        {
            this.handleKeyProperty(dataTypeProperty);
        }
        else if (dataTypeProperty.isDerived())
        {
            this.handleWarnIfPresent(dataTypeProperty, "derived");
        }
        else if (dataTypeProperty.isTemporal())
        {
            this.handleWarnIfPresent(dataTypeProperty, "temporal");
        }
        else if (dataTypeProperty.isAudit())
        {
            this.handleWarnIfPresent(dataTypeProperty, "audit");
        }
        else if (dataTypeProperty.isForeignKey())
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key");
        }
        else
        {
            this.handlePlainProperty(dataTypeProperty);
        }
    }

    private void handleIdProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (this.operationMode == OperationMode.CREATE)
        {
            return;
        }

        if (this.operationMode == OperationMode.REPLACE && this.isRoot)
        {
            return;
        }

        // TODO: Keep track of path used to get here. Key properties should be required for final to-one required properties
        return;
    }

    private void handleKeyProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        // TODO: Handle foreign key properties that are also key properties at the root

        if (this.isForeignKeyMatchingKeyOnPath(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key matching key on path");
            return;
        }

        // TODO: Exclude path here
        if (this.isForeignKeyMatchingRequiredNested(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key matching key of required nested object");
            return;
        }

        if (this.isRoot)
        {
            this.handleWarnIfPresent(dataTypeProperty, "root key");
            return;
        }

        if (this.isForeignKeyWithOpposite(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key");
            return;
        }

        this.handlePlainProperty(dataTypeProperty);
    }

    private boolean isForeignKeyMatchingRequiredNested(DataTypeProperty dataTypeProperty)
    {
        // TODO: Exclude path here
        return dataTypeProperty.getKeysMatchingThisForeignKey().keysView().anySatisfy(this::isToOneRequired);
    }

    private boolean isToOneRequired(AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();
        return multiplicity.isToOne() && multiplicity.isRequired();
    }

    private boolean isForeignKeyMatchingKeyOnPath(DataTypeProperty dataTypeProperty)
    {
        Optional<AssociationEnd>                                    opposite                   = this.pathHere.map(AssociationEnd::getOpposite);
        OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey = dataTypeProperty.getKeysMatchingThisForeignKey();
        return opposite
                .map(keysMatchingThisForeignKey::containsKey)
                .orElse(false);
    }

    private boolean isForeignKeyWithOpposite(@Nonnull DataTypeProperty keyProperty)
    {
        OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();
        ImmutableList<DataTypeProperty> dataTypeProperties = keysMatchingThisForeignKey
                .valuesView()
                .flatCollect(x -> x)
                .toList()
                .toImmutable();
        return dataTypeProperties
                .anySatisfyWith(this::isOppositeKey, keyProperty);
    }

    private boolean isOppositeKey(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull DataTypeProperty keyProperty)
    {
        return dataTypeProperty
                .getForeignKeysMatchingThisKey()
                .containsValue(Lists.immutable.with(keyProperty));
    }

    private void handleWarnIfPresent(@Nonnull DataTypeProperty property, String propertyKind)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode())
        {
            return;
        }

        if (jsonNode.isNull())
        {
            String warning = String.format(
                    "Warning at %s. Didn't expect to receive value for %s property '%s.%s: %s%s' but value was null.",
                    this.getContextString(),
                    propertyKind,
                    property.getOwningClassifier().getName(),
                    property.getName(),
                    property.getType(),
                    property.isOptional() ? "?" : "");
            this.warnings.add(warning);
            return;
        }

        String warning = String.format(
                "Warning at %s. Didn't expect to receive value for %s property '%s.%s: %s%s' but value was %s: %s.",
                this.getContextString(),
                propertyKind,
                property.getOwningClassifier().getName(),
                property.getName(),
                property.getType(),
                property.isOptional() ? "?" : "",
                jsonNode.getNodeType().toString().toLowerCase(),
                jsonNode);
        this.warnings.add(warning);
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

    private void handlePlainProperty(@Nonnull DataTypeProperty property)
    {
        if (!property.isRequired())
        {
            return;
        }

        if (this.operationMode == OperationMode.PATCH)
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
    //endregion

    //region AssociationEnds
    private void handleAssociationEnds()
    {
        for (AssociationEnd associationEnd : this.klass.getAssociationEnds())
        {
            if (this.isBackward(associationEnd))
            {
                this.handleWarnIfPresent(associationEnd, "opposite");
            }
            else if (associationEnd.isVersion())
            {
                this.handleVersionAssociationEnd(associationEnd);
            }
            else if (associationEnd.isOwned())
            {
                this.handleAssociationEnd(associationEnd);
            }
        }

        /*
        ImmutableList<AssociationEnd> sharedAssociationEnds = this.klass.getAssociationEnds()
                .reject(AssociationEnd::isOwned);
        for (AssociationEnd sharedAssociationEnd : sharedAssociationEnds)
        {
            if (sharedAssociationEnd.getMultiplicity().isToOne() && sharedAssociationEnd.isRequired())
            {
                throw new AssertionError(
                        "TODO: Required, shared, to-one, that's not opposite the path here requires embedded object with primary key present");
            }
        }
        */
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

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd, @Nonnull ObjectNode objectNode)
    {
        OperationMode nextMode = this.getNextMode(this.operationMode, associationEnd);

        if (associationEnd.isVersion())
        {
            this.handleVersionAssociationEnd(associationEnd);
        }
        else
        {
            this.handlePlainAssociationEnd(associationEnd, objectNode, nextMode);
        }
    }

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();

        JsonNode jsonNode = this.objectNode.path(associationEnd.getName());

        if ((jsonNode.isMissingNode() || jsonNode.isNull()) && multiplicity.isRequired())
        {
            if (this.operationMode == OperationMode.CREATE && associationEnd.isVersion())
            {
                return;
            }

            if (this.operationMode == OperationMode.REPLACE && associationEnd.isFinal())
            {
                String warning = String.format(
                        "Warning at %s. Expected value for required final property '%s.%s: %s[%s]' but value was %s.",
                        this.getContextString(),
                        associationEnd.getOwningClassifier().getName(),
                        associationEnd.getName(),
                        associationEnd.getType(),
                        associationEnd.getMultiplicity().getPrettyName(),
                        jsonNode.getNodeType().toString().toLowerCase());
                this.warnings.add(warning);
                return;
            }

            if (this.operationMode == OperationMode.REPLACE && associationEnd.isPrivate())
            {
                String warning = String.format(
                        "Warning at %s. Expected value for required private property '%s.%s: %s[%s]' but value was %s.",
                        this.getContextString(),
                        associationEnd.getOwningClassifier().getName(),
                        associationEnd.getName(),
                        associationEnd.getType(),
                        associationEnd.getMultiplicity().getPrettyName(),
                        jsonNode.getNodeType().toString().toLowerCase());
                this.warnings.add(warning);
                return;
            }

            String error = String.format(
                    "Error at %s. Expected value for required property '%s.%s: %s[%s]' but value was %s.",
                    this.getContextString(),
                    associationEnd.getOwningClassifier().getName(),
                    associationEnd.getName(),
                    associationEnd.getType(),
                    associationEnd.getMultiplicity().getPrettyName(),
                    jsonNode.getNodeType().toString().toLowerCase());
            this.errors.add(error);
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
            @Nonnull AssociationEnd associationEnd,
            JsonNode jsonNode)
    {
        this.contextStack.push(associationEnd.getName());
        try
        {
            if (jsonNode instanceof ObjectNode)
            {
                this.handleAssociationEnd(associationEnd, (ObjectNode) jsonNode);
            }
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    public void handleToMany(
            @Nonnull AssociationEnd associationEnd,
            JsonNode jsonNode)
    {
        if (!(jsonNode instanceof ArrayNode))
        {
            return;
        }

        for (int index = 0; index < jsonNode.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    associationEnd.getName(),
                    index);
            this.contextStack.push(contextString);

            try
            {
                JsonNode childJsonNode = jsonNode.path(index);
                if (childJsonNode instanceof ObjectNode)
                {
                    this.handleAssociationEnd(associationEnd, (ObjectNode) childJsonNode);
                }
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    private void handleVersionAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        if (this.operationMode == OperationMode.CREATE)
        {
            // TODO: recurse and error only if the version number isn't 1
            this.handleWarnIfPresent(associationEnd, "version");
        }
        else if (this.operationMode == OperationMode.REPLACE || this.operationMode == OperationMode.PATCH)
        {
            if (this.klass.getKeyProperties().anySatisfy(DataTypeProperty::isID))
            {
                // Classes with ID properties use separate endpoints for create and replace, so we know we're definitely replacing. Therefore the version must be present.
                this.handleErrorIfAbsent(associationEnd, "version");
            }

            // Classes without ID properties use a single endpoint for create and replace. We won't know if we're performing a replacement until querying from the data store. At this point, it's too early to validate anything.
        }
        else
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".handleVersionAssociationEnd() not implemented yet");
        }
    }

    private void handlePlainAssociationEnd(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode nextMode)
    {
        RequiredPropertiesValidator validator = new RequiredPropertiesValidator(
                associationEnd.getType(),
                objectNode,
                nextMode,
                this.errors,
                this.warnings,
                this.contextStack,
                Optional.of(associationEnd),
                false);
        validator.validate();
    }

    @Nonnull
    private OperationMode getNextMode(OperationMode operationMode, @Nonnull AssociationEnd associationEnd)
    {
        if (operationMode == OperationMode.CREATE && associationEnd.isOwned())
        {
            return OperationMode.CREATE;
        }

        if (operationMode == OperationMode.REPLACE && associationEnd.isOwned())
        {
            return OperationMode.REPLACE;
        }

        if (operationMode == OperationMode.PATCH && associationEnd.isOwned())
        {
            return OperationMode.PATCH;
        }

        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getNextMode() not implemented yet: " + operationMode);
    }

    private String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }

    private ImmutableList<Object> getKeysFromJsonNode(
            @Nonnull JsonNode jsonNode,
            @Nonnull Klass klass)
    {
        return klass
                .getKeyProperties()
                .reject(DataTypeProperty::isID)
                .collect(keyProperty -> this.getKeyFromJsonNode(
                        keyProperty,
                        jsonNode));
    }

    private ImmutableList<Object> getKeysFromJsonNode(
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode parentJsonNode)
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

    @Nullable
    private Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode)
    {
        OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

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

        return JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                keyProperty,
                (ObjectNode) jsonNode);
    }

    private Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode parentJsonNode)
    {
        OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        ImmutableList<DataTypeProperty> oppositeForeignKeys = keysMatchingThisForeignKey.get(opposite);

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

        Object result = JsonDataTypeValueVisitor.extractDataTypePropertyFromJson(
                keyProperty,
                (ObjectNode) jsonNode);
        return Objects.requireNonNull(result);
    }

    private boolean isBackward(@Nonnull AssociationEnd associationEnd)
    {
        return this.pathHere.equals(Optional.of(associationEnd.getOpposite()));
    }
}
