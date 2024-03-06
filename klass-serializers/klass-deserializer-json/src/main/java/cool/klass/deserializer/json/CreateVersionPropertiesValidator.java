package cool.klass.deserializer.json;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Stacks;

public class CreateVersionPropertiesValidator
{
    @Nonnull
    protected final Klass                    klass;
    @Nonnull
    protected final ObjectNode               objectNode;
    @Nonnull
    protected final OperationMode            operationMode;
    @Nonnull
    protected final MutableList<String>      errors;
    @Nonnull
    protected final MutableList<String>      warnings;
    @Nonnull
    protected final MutableStack<String>     contextStack;
    @Nonnull
    protected final Optional<AssociationEnd> pathHere;
    protected final boolean                  isRoot;

    public CreateVersionPropertiesValidator(
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        this.klass         = Objects.requireNonNull(klass);
        this.objectNode    = Objects.requireNonNull(objectNode);
        this.operationMode = Objects.requireNonNull(operationMode);
        this.errors        = Objects.requireNonNull(errors);
        this.warnings      = Objects.requireNonNull(warnings);
        this.contextStack  = Objects.requireNonNull(contextStack);
        this.pathHere      = Objects.requireNonNull(pathHere);
        this.isRoot        = isRoot;
    }

    public static void validate(
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings)
    {
        CreateVersionPropertiesValidator validator = new CreateVersionPropertiesValidator(
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
    protected void handleDataTypeProperties()
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = this.klass.getDataTypeProperties();
        for (DataTypeProperty dataTypeProperty : dataTypeProperties)
        {
            this.contextStack.push(dataTypeProperty.getName());
            try
            {
                this.handleDataTypeProperty(dataTypeProperty);
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    protected void handleDataTypeProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isID())
        {
            this.handleIdProperty(dataTypeProperty);
        }
        else if (dataTypeProperty.isKey())
        {
            this.handleKeyProperty(dataTypeProperty);
        }
        else if (dataTypeProperty.isAudit())
        {
            if (dataTypeProperty.isPrivate())
            {
                this.handleErrorIfPresent(dataTypeProperty, "audit");
            }
            else
            {
                this.handleWarnIfPresent(dataTypeProperty, "audit");
            }
        }
        else if (dataTypeProperty.isForeignKey())
        {
            if (dataTypeProperty.isPrivate())
            {
                this.handleErrorIfPresent(dataTypeProperty, "foreign key");
            }
            else
            {
                this.handleWarnIfPresent(dataTypeProperty, "foreign key");
            }
        }
        else if (dataTypeProperty.isPrivate())
        {
            this.handleErrorIfPresent(dataTypeProperty, "private");
        }
        else if (dataTypeProperty.isDerived())
        {
            this.handleWarnIfPresent(dataTypeProperty, "derived");
        }
        else if (dataTypeProperty.getType() == PrimitiveType.TEMPORAL_RANGE)
        {
            this.handleErrorIfPresent(dataTypeProperty, "temporal range");
        }
        else if (dataTypeProperty.getType() == PrimitiveType.TEMPORAL_INSTANT)
        {
            this.handleWarnIfPresent(dataTypeProperty, "temporal");
        }
        else if (dataTypeProperty.isVersion())
        {
            this.handleVersionProperty(dataTypeProperty);
        }
        else
        {
            this.handlePlainProperty(dataTypeProperty);
        }
    }

    protected void handleIdProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (this.operationMode == OperationMode.CREATE)
        {
            return;
        }

        if (this.operationMode == OperationMode.REPLACE && this.isRoot)
        {
            return;
        }

        if (this.pathHere.isPresent() && this.pathHere.get().getMultiplicity() == Multiplicity.ONE_TO_ONE)
        {
            JsonNode jsonNode = this.objectNode.path(dataTypeProperty.getName());
            if (jsonNode.isMissingNode() || jsonNode.isNull())
            {
                String error = String.format(
                        "Error at %s. Expected value for required id property '%s.%s: %s%s' but value was %s.",
                        this.getContextString(),
                        dataTypeProperty.getOwningClassifier().getName(),
                        dataTypeProperty.getName(),
                        dataTypeProperty.getType(),
                        dataTypeProperty.isOptional() ? "?" : "",
                        jsonNode.getNodeType().toString().toLowerCase());
                this.errors.add(error);
            }
        }
    }

    protected void handleKeyProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        // TODO: Handle foreign key properties that are also key properties at the root
        if (this.isForeignKeyMatchingKeyOnPath(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key matching key on path");
            return;
        }

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

        if (dataTypeProperty.isForeignKeyWithOpposite())
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key");
            return;
        }

        if (this.pathHere.isPresent() && dataTypeProperty.isForeignKeyMatchingKeyOnPath(this.pathHere.get()))
        {
            this.handleWarnIfPresent(this.pathHere.get());
            return;
        }

        this.handlePlainProperty(dataTypeProperty);
    }

    protected boolean isForeignKeyMatchingRequiredNested(DataTypeProperty dataTypeProperty)
    {
        // TODO: Exclude path here
        return dataTypeProperty.getKeysMatchingThisForeignKey().keysView().anySatisfy(this::isToOneRequired);
    }

    protected boolean isToOneRequired(AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();
        return multiplicity.isToOne() && multiplicity.isRequired();
    }

    protected boolean isForeignKeyMatchingKeyOnPath(DataTypeProperty dataTypeProperty)
    {
        return this.pathHere.map(dataTypeProperty::isForeignKeyMatchingKeyOnPath).orElse(false);
    }

    protected void handleAnnotationIfPresent(
            @Nonnull DataTypeProperty property,
            String propertyKind,
            MutableList<String> annotations,
            String severityString)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode())
        {
            return;
        }

        String jsonNodeString = jsonNode.isNull() ? "" : ": " + jsonNode;
        String annotation = String.format(
                "%s at %s. Didn't expect to receive value for %s property '%s.%s: %s%s' but value was %s%s.",
                severityString,
                this.getContextString(),
                propertyKind,
                property.getOwningClassifier().getName(),
                property.getName(),
                property.getType(),
                property.isOptional() ? "?" : "",
                jsonNode.getNodeType().toString().toLowerCase(),
                jsonNodeString);
        annotations.add(annotation);
    }

    protected void handleErrorIfPresent(@Nonnull DataTypeProperty dataTypeProperty, String propertyKind)
    {
        this.handleAnnotationIfPresent(dataTypeProperty, propertyKind, this.errors, "Error");
    }

    protected void handleWarnIfPresent(@Nonnull DataTypeProperty dataTypeProperty, String propertyKind)
    {
        this.handleAnnotationIfPresent(dataTypeProperty, propertyKind, this.warnings, "Warning");
    }

    protected void handleWarnIfPresent(@Nonnull AssociationEnd property)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode())
        {
            return;
        }

        String jsonNodeString = jsonNode.isNull() ? "" : ": " + jsonNode;
        String warning = String.format(
                "Warning at %s. Didn't expect to receive value for association end on version class '%s.%s: %s[%s]' but value was %s%s.",
                this.getContextString(),
                property.getOwningClassifier().getName(),
                property.getName(),
                property.getType(),
                property.getMultiplicity().getPrettyName(),
                jsonNode.getNodeType().toString().toLowerCase(),
                jsonNodeString);
        this.warnings.add(warning);
    }

    protected void handlePlainProperty(@Nonnull DataTypeProperty property)
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

    private void handleVersionProperty(DataTypeProperty property)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode() || jsonNode.isNull())
        {
            return;
        }

        if (!jsonNode.isIntegralNumber())
        {
            return;
        }

        if (jsonNode.asInt() != 1)
        {
            String error = String.format(
                    "Error at %s. Expected value for version property '%s.%s: %s%s' to be 1 during initial creation but value was %s.",
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
    protected void handleAssociationEnds()
    {
        for (AssociationEnd associationEnd : this.klass.getAssociationEnds())
        {
            this.handleAssociationEnd(associationEnd);
        }
    }

    protected void handleAssociationEnd(AssociationEnd associationEnd)
    {
        this.handleWarnIfPresent(associationEnd);
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
                this.handleOwnedAssociationEnd(associationEnd, (ObjectNode) jsonNode);
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
                    this.handleOwnedAssociationEnd(associationEnd, (ObjectNode) childJsonNode);
                }
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    protected void handleErrorIfAbsent(@Nonnull AssociationEnd associationEnd, String propertyKind)
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

    protected void handleOwnedAssociationEnd(@Nonnull AssociationEnd associationEnd, @Nonnull ObjectNode objectNode)
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

    protected void handleOwnedAssociationEnd(@Nonnull AssociationEnd associationEnd)
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

            if (this.operationMode == OperationMode.CREATE
                    || this.operationMode == OperationMode.REPLACE
                    || this.operationMode == OperationMode.PATCH && jsonNode.isNull())
            {
                String error = String.format(
                        "Error at %s. Expected value for required property '%s.%s: %s[%s]' but value was %s.",
                        this.getContextString(),
                        associationEnd.getOwningClassifier().getName(),
                        associationEnd.getName(),
                        associationEnd.getType(),
                        associationEnd.getMultiplicity().getPrettyName(),
                        jsonNode.getNodeType().toString().toLowerCase());
                this.errors.add(error);
                return;
            }
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

    protected void handleVersionAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        if (this.operationMode == OperationMode.CREATE)
        {
            // TODO: recurse and error only if the version number isn't 1
            this.handleWarnIfPresent(associationEnd);
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
        else if (this.operationMode == OperationMode.REFERENCE_OUTSIDE_PROJECTION)
        {
            // TODO: Recurse and check that it matches if present
            this.handleWarnIfPresent(associationEnd);
        }
        else
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".handleVersionAssociationEnd() not implemented yet");
        }
    }

    protected void handlePlainAssociationEnd(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode nextMode)
    {
        CreateVersionPropertiesValidator validator = new CreateVersionPropertiesValidator(
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
    protected OperationMode getNextMode(OperationMode operationMode, @Nonnull AssociationEnd associationEnd)
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
        if ((operationMode == OperationMode.CREATE || operationMode == OperationMode.PATCH || operationMode == OperationMode.REPLACE) && !associationEnd.isOwned())
        {
            return OperationMode.REFERENCE_OUTSIDE_PROJECTION;
        }
        if (operationMode == OperationMode.REFERENCE_OUTSIDE_PROJECTION)
        {
            return OperationMode.REFERENCE_OUTSIDE_PROJECTION;
        }

        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getNextMode() not implemented yet: " + operationMode);
    }

    protected String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }

    protected ImmutableList<Object> getKeysFromJsonNode(
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

    protected Object getKeyFromJsonNode(
            @Nonnull DataTypeProperty keyProperty,
            @Nonnull JsonNode jsonNode,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull JsonNode parentJsonNode)
    {
        OrderedMap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = keyProperty.getKeysMatchingThisForeignKey();

        AssociationEnd opposite = associationEnd.getOpposite();

        DataTypeProperty oppositeForeignKey = keysMatchingThisForeignKey.get(opposite);

        if (oppositeForeignKey != null)
        {
            String oppositeForeignKeyName = oppositeForeignKey.getName();
            Object result                 = parentJsonNode.path(oppositeForeignKeyName);
            return Objects.requireNonNull(result);
        }

        if (keysMatchingThisForeignKey.notEmpty())
        {
            if (keysMatchingThisForeignKey.size() != 1)
            {
                throw new AssertionError();
            }

            Pair<AssociationEnd, DataTypeProperty> pair = keysMatchingThisForeignKey.keyValuesView().getOnly();

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

    protected boolean isBackward(@Nonnull AssociationEnd associationEnd)
    {
        return this.pathHere.equals(Optional.of(associationEnd.getOpposite()));
    }
}
