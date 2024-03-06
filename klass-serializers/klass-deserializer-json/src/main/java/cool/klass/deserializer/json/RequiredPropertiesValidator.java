package cool.klass.deserializer.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Stacks;

public final class RequiredPropertiesValidator
{
    private final ObjectNode objectNode;
    private final Projection projection;
    private final OperationMode operationMode;
    private final MutableList<String> errors;

    public RequiredPropertiesValidator(
            ObjectNode objectNode,
            Projection projection,
            OperationMode operationMode,
            MutableList<String> errors)
    {
        this.objectNode = objectNode;
        this.projection = projection;
        this.operationMode = operationMode;
        this.errors = errors;
    }

    public static void validate(
            ObjectNode objectNode,
            Projection projection,
            OperationMode operationMode,
            MutableList<String> errors)
    {
        RequiredPropertiesValidator incomingDataValidator = new RequiredPropertiesValidator(
                objectNode,
                projection,
                operationMode,
                errors);
        incomingDataValidator.validateIncomingData();
    }

    public void validateIncomingData()
    {
        ProjectionListener listener = new RequiredPropertiesValidatingListener(
                this.objectNode,
                this.operationMode,
                this.errors,
                Stacks.mutable.empty());
        this.projection.visit(listener);
    }
}
