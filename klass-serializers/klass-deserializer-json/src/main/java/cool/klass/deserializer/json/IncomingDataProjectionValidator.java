package cool.klass.deserializer.json;

import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

public final class IncomingDataProjectionValidator
{
    private final ObjectNode objectNode;
    private final Projection projection;

    private final MutableStack<String> contextStack = Stacks.mutable.empty();
    private final MutableList<String>  errors;

    public IncomingDataProjectionValidator(ObjectNode objectNode, Projection projection, MutableList<String> errors)
    {
        this.objectNode = objectNode;
        this.projection = projection;
        this.errors = errors;
    }

    public static void validate(ObjectNode objectNode, Projection projection, MutableList<String> errors)
    {
        IncomingDataProjectionValidator incomingDataValidator = new IncomingDataProjectionValidator(
                objectNode,
                projection,
                errors);
        incomingDataValidator.validateIncomingData();
    }

    public void validateIncomingData()
    {
        IncomingDataProjectionListener listener = new IncomingDataProjectionListener(this.objectNode);
        this.visitProjectionElement(this.projection, listener);
    }

    public String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }

    public void visitProjectionElement(ProjectionElement projectionElement, IncomingDataProjectionListener listener)
    {
        try
        {
            projectionElement.enter(listener);
        }
        finally
        {
            projectionElement.exit(listener);
        }
    }

    private final class IncomingDataProjectionListener implements ProjectionListener
    {
        private final ObjectNode objectNode;

        private IncomingDataProjectionListener(ObjectNode objectNode)
        {
            this.objectNode = Objects.requireNonNull(objectNode);
        }

        @Override
        public void enterProjection(Projection projection)
        {
            IncomingDataProjectionValidator.this.contextStack.push(projection.getKlass().getName());
            for (ProjectionElement projectionElement: projection.getChildren())
            {
                IncomingDataProjectionValidator.this.visitProjectionElement(projectionElement, this);
            }
        }

        @Override
        public void exitProjection(Projection projection)
        {
            IncomingDataProjectionValidator.this.contextStack.pop();
        }

        @Override
        public void enterProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
        {
            DataTypeProperty property = projectionDataTypeProperty.getProperty();
            IncomingDataProjectionValidator.this.contextStack.push(property.getName());

            if (property.isOptional()
                    || property.isTemporal()
                    || property.isKey())
            {
                return;
            }

            JsonNode jsonNode = this.objectNode.path(property.getName());
            if (!jsonNode.isMissingNode() && !jsonNode.isNull())
            {
                return;
            }
            String error = String.format(
                    "Error at %s. Expected enumerated property with type '%s.%s: %s%s' but value was %s.",
                    IncomingDataProjectionValidator.this.getContextString(),
                    property.getOwningKlass().getName(),
                    property.getName(),
                    property.getType().toString(),
                    property.isOptional() ? "?" : "",
                    jsonNode.getNodeType().toString().toLowerCase());
            IncomingDataProjectionValidator.this.errors.add(error);
        }

        @Override
        public void exitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
        {
            IncomingDataProjectionValidator.this.contextStack.pop();
        }

        @Override
        public void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterProjectionAssociationEnd() not implemented yet");
        }

        @Override
        public void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".exitProjectionAssociationEnd() not implemented yet");
        }
    }
}
