package cool.klass.deserializer.json;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.BaseProjectionListener;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class RequiredPropertiesValidatingListener extends BaseProjectionListener
{
    private final ObjectNode           objectNode;
    private final OperationMode        operationMode;
    private final MutableList<String>  errors;
    private final MutableStack<String> contextStack;

    public RequiredPropertiesValidatingListener(
            ObjectNode objectNode,
            OperationMode operationMode,
            MutableList<String> errors,
            MutableStack<String> contextStack)
    {
        this.objectNode = Objects.requireNonNull(objectNode);
        this.operationMode = Objects.requireNonNull(operationMode);
        this.errors = Objects.requireNonNull(errors);
        this.contextStack = Objects.requireNonNull(contextStack);
    }

    @Override
    public void enterProjection(Projection projection)
    {
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
        AssociationEnd associationEnd = projectionAssociationEnd.getProperty();
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
        this.contextStack.push(projectionAssociationEnd.getProperty().getName());
        try
        {
            if (jsonNode instanceof ObjectNode)
            {
                this.handleEach(projectionAssociationEnd, (ObjectNode) jsonNode);
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
            return;
        }

        for (int index = 0; index < jsonNode.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    projectionAssociationEnd.getProperty().getName(),
                    index);
            this.contextStack.push(contextString);

            try
            {
                JsonNode childJsonNode = jsonNode.get(index);
                if (childJsonNode instanceof ObjectNode)
                {
                    this.handleEach(projectionAssociationEnd, (ObjectNode) childJsonNode);
                }
            }
            finally
            {
                this.contextStack.pop();
            }
        }
    }

    public void handleEach(ProjectionAssociationEnd projectionAssociationEnd, ObjectNode objectNode)
    {
        OperationMode nextMode = this.getNextMode(this.operationMode, projectionAssociationEnd.getProperty());

        ProjectionListener listener = new RequiredPropertiesValidatingListener(
                objectNode,
                nextMode,
                this.errors,
                this.contextStack);
        projectionAssociationEnd.visitChildren(listener);
    }

    private OperationMode getNextMode(OperationMode operationMode, AssociationEnd associationEnd)
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

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
    }

    private void handleDataTypeProperties(Klass klass, boolean isRoot)
    {
        ImmutableList<DataTypeProperty> idProperties       = klass.getDataTypeProperties().select(DataTypeProperty::isID);
        ImmutableList<DataTypeProperty> keyProperties      = klass.getKeyProperties().reject(DataTypeProperty::isID);
        ImmutableList<DataTypeProperty> plainProperties = klass.getDataTypeProperties()
                .reject(DataTypeProperty::isID)
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isTemporal)
                .reject(DataTypeProperty::isAudit);

        this.handleIdProperties(idProperties, isRoot);
        this.handleKeyProperties(keyProperties);
        this.handlePlainProperties(plainProperties);
    }

    private void handleIdProperties(ImmutableList<DataTypeProperty> idProperties, boolean isRoot)
    {
        if (this.operationMode == OperationMode.CREATE)
        {
            return;
        }

        if (this.operationMode == OperationMode.REPLACE && isRoot)
        {
            return;
        }

        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".handleIdProperties() not implemented yet");
    }

    private void handleKeyProperties(ImmutableList<DataTypeProperty> keyProperties)
    {
        for (DataTypeProperty keyProperty : keyProperties)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".handleKeyProperties() not implemented yet");
        }
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
