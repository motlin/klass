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
import cool.klass.model.meta.domain.api.projection.BaseProjectionListener;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class IncomingUpdateDataModelListener extends BaseProjectionListener
{
    private final DataStore            dataStore;
    private final Object               persistentInstance;
    private final ObjectNode           objectNode;
    private final ProjectionParent     projectionParent;
    private final MutableList<String>  errors;
    private final MutableStack<String> contextStack;
    private final boolean              isRoot;

    public IncomingUpdateDataModelListener(
            DataStore dataStore,
            Object persistentInstance,
            ObjectNode objectNode,
            ProjectionParent projectionParent,
            MutableList<String> errors,
            MutableStack<String> contextStack,
            boolean isRoot)
    {
        this.dataStore = dataStore;
        this.persistentInstance = Objects.requireNonNull(persistentInstance);
        this.objectNode = Objects.requireNonNull(objectNode);
        this.projectionParent = Objects.requireNonNull(projectionParent);
        this.errors = Objects.requireNonNull(errors);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.isRoot = projectionParent instanceof Projection;
        if (this.isRoot != isRoot)
        {
            throw new AssertionError("TODO: Simplify this assertion away.");
        }
    }

    @Override
    public void enterProjection(Projection projection)
    {
        if (!this.isRoot)
        {
            throw new AssertionError("TODO: Simplify this assertion away");
        }

        this.contextStack.push(projection.getKlass().getName());
        this.handleDataTypeProperties(projection.getKlass(), true);
        projection.visitChildren(this);
    }

    @Override
    public void exitProjection(Projection projection)
    {
        this.contextStack.pop();
    }

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        AssociationEnd associationEnd = projectionAssociationEnd.getAssociationEnd();
        Multiplicity   multiplicity   = associationEnd.getMultiplicity();

        JsonNode jsonNode = this.objectNode.path(associationEnd.getName());

        if ((jsonNode.isMissingNode() || jsonNode.isNull()) && multiplicity.isRequired())
        {
            throw new AssertionError();
        }

        if (multiplicity.isToOne())
        {
            this.handleToOne(projectionAssociationEnd, jsonNode);
        }
        else
        {
            this.handleToMany(projectionAssociationEnd, jsonNode);
        }
    }

    public void handleToOne(
            ProjectionAssociationEnd projectionAssociationEnd,
            JsonNode jsonNode)
    {
        this.contextStack.push(projectionAssociationEnd.getAssociationEnd().getName());
        try
        {
            if (jsonNode instanceof ObjectNode)
            {
                Object childPersistentInstance = this.dataStore.getToOne(
                        this.persistentInstance,
                        projectionAssociationEnd.getAssociationEnd());
                this.handleEach(projectionAssociationEnd, (ObjectNode) jsonNode, childPersistentInstance);
            }
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    public void handleToMany(
            ProjectionAssociationEnd projectionAssociationEnd,
            JsonNode jsonNode)
    {
        if (!(jsonNode instanceof ArrayNode))
        {
            this.contextStack.push(projectionAssociationEnd.getAssociationEnd().getName());
            String error = String.format(
                    "Error at %s. Expected json array but value was %s.",
                    this.getContextString(),
                    jsonNode.getNodeType().toString().toLowerCase());
            this.errors.add(error);
            return;
        }

        List<Object> childPersistentInstances = this.dataStore.getToMany(
                this.persistentInstance,
                projectionAssociationEnd.getAssociationEnd());

        for (int index = 0; index < jsonNode.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    projectionAssociationEnd.getAssociationEnd().getName(),
                    index);
            this.contextStack.push(contextString);

            try
            {
                JsonNode childJsonNode = jsonNode.get(index);
                Object   childPersistentInstance             = childPersistentInstances.get(index);
                if (childJsonNode instanceof ObjectNode)
                {
                    this.handleEach(projectionAssociationEnd, (ObjectNode) childJsonNode, childPersistentInstance);
                }
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    public void handleEach(
            ProjectionAssociationEnd projectionAssociationEnd,
            ObjectNode objectNode,
            Object persistentInstance)
    {
        ProjectionListener listener = new IncomingUpdateDataModelListener(
                this.dataStore,
                persistentInstance,
                objectNode,
                projectionAssociationEnd,
                this.errors,
                this.contextStack,
                false);
        projectionAssociationEnd.visitChildren(listener);
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
    }

    private void handleDataTypeProperties(Klass klass, boolean isRoot)
    {
        ImmutableList<DataTypeProperty> plainProperties = klass.getDataTypeProperties()
                .reject(DataTypeProperty::isID)
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isTemporal)
                .reject(DataTypeProperty::isAudit);

        if (this.isRoot != isRoot)
        {
            throw new AssertionError();
        }

        this.handleIdProperties(klass.getKeyProperties(), isRoot);
        this.checkPresentPropertiesMatch(klass.getDataTypeProperties().select(DataTypeProperty::isTemporal));
        this.checkPresentPropertiesMatch(klass.getDataTypeProperties().select(DataTypeProperty::isAudit));
        this.checkRequiredPropertiesPresent(plainProperties);
    }

    private void handleIdProperties(ImmutableList<DataTypeProperty> idProperties, boolean isRoot)
    {
        if (this.isRoot != isRoot)
        {
            throw new AssertionError();
        }

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

    @Override
    public void enterProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        DataTypeProperty property = projectionDataTypeProperty.getProperty();
        this.contextStack.push(property.getName());

        // TODO: Check root
        if (property.isOptional()
                || property.isTemporal()
                || property.isKey())
        {
            return;
        }

        this.handlePlainProperty(property);
    }

    @Override
    public void exitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        this.contextStack.pop();
    }

    public String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }
}
