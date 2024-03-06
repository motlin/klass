package cool.klass.reladomo.persistent.writer;

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
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

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
            JsonNode jsonNode)
    {
        if (!(jsonNode instanceof ArrayNode))
        {
            this.contextStack.push(associationEnd.getName());
            String error = String.format(
                    "Error at %s. Expected json array but value was %s.",
                    this.getContextString(),
                    jsonNode.getNodeType().toString().toLowerCase());
            this.errors.add(error);
            this.contextStack.pop();
            return;
        }

        List<Object> childPersistentInstances = this.dataStore.getToMany(
                this.persistentInstance,
                associationEnd);

        for (int index = 0; index < jsonNode.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    associationEnd.getName(),
                    index);
            this.contextStack.push(contextString);

            try
            {
                JsonNode childJsonNode           = jsonNode.get(index);
                Object   childPersistentInstance = childPersistentInstances.get(index);
                if (childJsonNode instanceof ObjectNode)
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
                .reject(DataTypeProperty::isAudit);

        this.handleIdProperties(this.klass.getDataTypeProperties().select(DataTypeProperty::isID));
        this.checkPresentPropertiesMatch(this.klass.getDataTypeProperties().select(DataTypeProperty::isTemporal));
        this.checkPresentPropertiesMatch(this.klass.getDataTypeProperties().select(DataTypeProperty::isAudit));
        this.checkRequiredPropertiesPresent(plainProperties);
    }

    private void handleIdProperties(ImmutableList<DataTypeProperty> idProperties)
    {
        this.checkPresentPropertiesMatch(idProperties);
        if (!isRoot)
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
}
