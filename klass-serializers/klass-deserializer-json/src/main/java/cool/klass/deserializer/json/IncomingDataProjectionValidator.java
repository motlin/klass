package cool.klass.deserializer.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionListener;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Stacks;

public final class IncomingDataProjectionValidator
{
    private final ObjectNode objectNode;
    private final Projection projection;

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
        ProjectionListener listener = new IncomingDataProjectionListener(
                this.objectNode,
                this.errors, Stacks.mutable.empty());
        this.projection.visit(listener);
    }
}
