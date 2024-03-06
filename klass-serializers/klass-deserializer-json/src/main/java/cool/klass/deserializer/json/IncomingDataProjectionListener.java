package cool.klass.deserializer.json;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class IncomingDataProjectionListener implements ProjectionListener
{
    private final MutableStack<String> contextStack;

    private final JsonNode            jsonNode;
    private final MutableList<String> errors;

    public IncomingDataProjectionListener(
            JsonNode jsonNode,
            MutableList<String> errors,
            MutableStack<String> contextStack)
    {
        this.jsonNode = Objects.requireNonNull(jsonNode);
        this.errors = Objects.requireNonNull(errors);
        this.contextStack = contextStack;
    }

    @Override
    public void enterProjection(Projection projection)
    {
        this.contextStack.push(projection.getKlass().getName());
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

        JsonNode jsonNode = this.jsonNode.path(associationEnd.getName());
        // TODO: Required check
        // if (!jsonNode.isMissingNode() && !jsonNode.isNull())
        // {
        // }

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
        if (jsonNode instanceof ObjectNode)
        {
            this.handleEach(projectionAssociationEnd, jsonNode);
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

        for (int index = 0; index < jsonNode.size(); index++)
        {
            String contextString = String.format(
                    "%s[%d]",
                    projectionAssociationEnd.getAssociationEnd().getName(),
                    index);
            this.contextStack.push(contextString);

            JsonNode childJsonNode = jsonNode.get(index);
            if (childJsonNode instanceof ObjectNode)
            {
                this.handleEach(projectionAssociationEnd, childJsonNode);
            }
        }
    }

    public void handleEach(ProjectionAssociationEnd projectionAssociationEnd, JsonNode jsonNode)
    {
        ProjectionListener listener = new IncomingDataProjectionListener(jsonNode, this.errors, this.contextStack);
        projectionAssociationEnd.visitChildren(listener);
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        JsonNode jsonNode = this.jsonNode.path(projectionAssociationEnd.getAssociationEnd().getName());
        if (!jsonNode.isMissingNode() && !jsonNode.isNull())
        {
            this.contextStack.pop();
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

        JsonNode jsonNode = this.jsonNode.path(property.getName());
        if (jsonNode.isMissingNode() || jsonNode.isNull())
        {
            String error = String.format(
                    "Error at %s. Expected value for required property '%s.%s: %s%s' but value was %s.",
                    this.getContextString(),
                    property.getOwningKlass().getName(),
                    property.getName(),
                    property.getType().toString(),
                    property.isOptional() ? "?" : "",
                    jsonNode.getNodeType().toString().toLowerCase());
            this.errors.add(error);
        }
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
