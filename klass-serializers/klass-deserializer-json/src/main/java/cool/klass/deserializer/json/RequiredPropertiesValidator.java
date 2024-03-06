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
    private final MutableStack<String> contextStack;
    private final boolean              isRoot;

    public RequiredPropertiesValidator(
            Klass klass,
            ObjectNode objectNode,
            OperationMode operationMode,
            MutableList<String> errors,
            MutableStack<String> contextStack,
            boolean isRoot)
    {
        this.klass = Objects.requireNonNull(klass);
        this.objectNode = Objects.requireNonNull(objectNode);
        this.operationMode = Objects.requireNonNull(operationMode);
        this.errors = Objects.requireNonNull(errors);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.isRoot = isRoot;
    }

    public static void validate(
            @Nonnull Klass klass,
            @Nonnull ObjectNode incomingInstance,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors)
    {
        RequiredPropertiesValidator validator = new RequiredPropertiesValidator(
                klass,
                incomingInstance,
                operationMode,
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
            AssociationEnd associationEnd,
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

    public void handleAssociationEnd(@Nonnull AssociationEnd associationEnd, ObjectNode objectNode)
    {
        OperationMode nextMode = this.getNextMode(this.operationMode, associationEnd);

        RequiredPropertiesValidator validator = new RequiredPropertiesValidator(
                associationEnd.getType(),
                objectNode,
                nextMode,
                this.errors,
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
        ImmutableList<DataTypeProperty> plainProperties = this.klass.getDataTypeProperties()
                .reject(DataTypeProperty::isID)
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isTemporal)
                .reject(DataTypeProperty::isAudit);

        this.handleIdProperties(this.klass.getDataTypeProperties().select(DataTypeProperty::isID));
        this.handleKeyProperties(this.klass.getKeyProperties().reject(DataTypeProperty::isID));
        this.handlePlainProperties(plainProperties);
    }

    private void handleIdProperties(ImmutableList<DataTypeProperty> idProperties)
    {
        if (this.operationMode == OperationMode.CREATE)
        {
            return;
        }

        if (this.operationMode == OperationMode.REPLACE && this.isRoot)
        {
            return;
        }

        for (DataTypeProperty idProperty : idProperties)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".handleIdProperties() not implemented yet");
        }
    }

    private void handleKeyProperties(ImmutableList<DataTypeProperty> keyProperties)
    {
        keyProperties
                .reject(this::isForeignKeyWithoutOpposite)
                .each(this::handlePlainProperty);
    }

    private boolean isForeignKeyWithoutOpposite(DataTypeProperty keyProperty)
    {
        return keyProperty.getKeysMatchingThisForeignKey()
                .valuesView()
                .noneSatisfyWith(this::isOppositeKey, keyProperty);
    }

    private boolean isOppositeKey(
            DataTypeProperty dataTypeProperty,
            DataTypeProperty keyProperty)
    {
        return dataTypeProperty.getKeysMatchingThisForeignKey().containsValue(keyProperty);
    }

    private void handlePlainProperties(ImmutableList<DataTypeProperty> plainProperties)
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

    private void handlePlainProperty(@Nonnull DataTypeProperty property)
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
}
