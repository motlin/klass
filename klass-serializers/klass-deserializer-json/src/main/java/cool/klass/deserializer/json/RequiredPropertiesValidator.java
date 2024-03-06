package cool.klass.deserializer.json;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

public class RequiredPropertiesValidator
{
    private final Klass                klass;
    private final ObjectNode           objectNode;
    private final OperationMode        operationMode;
    private final MutableList<String>  errors;
    private final MutableList<String>  warnings;
    private final MutableStack<String> contextStack;
    private final boolean              isRoot;

    public RequiredPropertiesValidator(
            Klass klass,
            ObjectNode objectNode,
            OperationMode operationMode,
            MutableList<String> errors,
            MutableList<String> warnings,
            MutableStack<String> contextStack,
            boolean isRoot)
    {
        this.klass = Objects.requireNonNull(klass);
        this.objectNode = Objects.requireNonNull(objectNode);
        this.operationMode = Objects.requireNonNull(operationMode);
        this.errors = Objects.requireNonNull(errors);
        this.warnings = Objects.requireNonNull(warnings);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.isRoot = isRoot;
    }

    public static void validate(
            @Nonnull Klass klass,
            @Nonnull ObjectNode incomingInstance,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings)
    {
        RequiredPropertiesValidator validator = new RequiredPropertiesValidator(
                klass,
                incomingInstance,
                operationMode,
                errors,
                warnings,
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

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        Multiplicity multiplicity = associationEnd.getMultiplicity();

        JsonNode jsonNode = this.objectNode.path(associationEnd.getName());

        if ((jsonNode.isMissingNode() || jsonNode.isNull()) && multiplicity.isRequired())
        {
            if (this.operationMode == OperationMode.CREATE && associationEnd.isVersion())
            {
                // TODO: Should we actually throw here? Or allow a version as long as its id is 1?
                return;
            }

            String error = String.format(
                    "Error at %s. Expected value for required property '%s.%s: %s[%s]' but value was %s.",
                    this.getContextString(),
                    associationEnd.getOwningClassifier().getName(),
                    associationEnd.getName(),
                    associationEnd.getType().toString(),
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
                JsonNode childJsonNode = jsonNode.get(index);
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

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd, ObjectNode objectNode)
    {
        OperationMode nextMode = this.getNextMode(this.operationMode, associationEnd);

        RequiredPropertiesValidator validator = new RequiredPropertiesValidator(
                associationEnd.getType(),
                objectNode,
                nextMode,
                this.errors,
                this.warnings,
                this.contextStack,
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

        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getNextMode() not implemented yet");
    }

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

        if (this.isForeignKeyWithOpposite(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key");
            return;
        }

        if (this.isRoot)
        {
            this.handleWarnIfPresent(dataTypeProperty, "root key");
            return;
        }

        this.handlePlainProperty(dataTypeProperty);
    }

    private boolean isForeignKeyWithOpposite(@Nonnull DataTypeProperty keyProperty)
    {
        return keyProperty.getKeysMatchingThisForeignKey()
                .valuesView()
                .anySatisfyWith(this::isOppositeKey, keyProperty);
    }

    private boolean isOppositeKey(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull DataTypeProperty keyProperty)
    {
        return dataTypeProperty.getKeysMatchingThisForeignKey().containsValue(keyProperty);
    }

    private void handleWarnIfPresent(DataTypeProperty property, String propertyKind)
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
                    property.getType().toString(),
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
                property.getType().toString(),
                property.isOptional() ? "?" : "",
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

    private void handleAssociationEnds()
    {
        ImmutableList<AssociationEnd> ownedAssociationEnds = this.klass.getAssociationEnds()
                .select(AssociationEnd::isOwned);

        for (AssociationEnd associationEnd : ownedAssociationEnds)
        {
            this.contextStack.push(associationEnd.getName());

            try
            {
                this.handleAssociationEnd(associationEnd);
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    private String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }
}
