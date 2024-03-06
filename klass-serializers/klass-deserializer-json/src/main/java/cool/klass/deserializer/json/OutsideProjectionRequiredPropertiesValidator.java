package cool.klass.deserializer.json;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class OutsideProjectionRequiredPropertiesValidator
        extends RequiredPropertiesValidator
{
    public OutsideProjectionRequiredPropertiesValidator(
            @Nonnull Klass klass,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode operationMode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere,
            boolean isRoot)
    {
        super(klass, objectNode, operationMode, errors, warnings, contextStack, pathHere, isRoot, false);
    }

    @Override
    protected void handleAssociationEndOutsideProjection(AssociationEnd associationEnd)
    {
        if (!associationEnd.isRequired())
        {
            return;
        }

        this.handleAnnotationIfPresent(associationEnd, "outside projection");
    }

    @Override
    protected void handlePlainAssociationEnd(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ObjectNode objectNode,
            @Nonnull OperationMode nextMode)
    {
        RequiredPropertiesValidator validator = new OutsideProjectionRequiredPropertiesValidator(
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
}
