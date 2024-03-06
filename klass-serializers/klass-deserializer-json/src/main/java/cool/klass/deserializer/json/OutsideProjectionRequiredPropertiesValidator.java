package cool.klass.deserializer.json;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
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
        super(klass, objectNode, operationMode, errors, warnings, contextStack, pathHere, isRoot);
    }

    @Override
    protected void handleKeyProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (this.isForeignKeyMatchingKeyOnPath(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key matching key on path");
            return;
        }

        // TODO: Exclude path here
        if (this.isForeignKeyMatchingRequiredNested(dataTypeProperty))
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key matching key of required nested object");
            return;
        }

        if (this.isRoot)
        {
            this.handleWarnIfPresent(dataTypeProperty, "root key");
            return;
        }

        if (dataTypeProperty.isForeignKeyWithOpposite())
        {
            this.handleWarnIfPresent(dataTypeProperty, "foreign key");
            return;
        }

        if (this.pathHere.isPresent() && dataTypeProperty.isForeignKeyMatchingKeyOnPath(this.pathHere.get()))
        {
            this.handleWarnIfPresent(this.pathHere.get(), dataTypeProperty.getName());
            return;
        }

        if (!this.objectNode.has(dataTypeProperty.getName()))
        {
            String error = String.format(
                    "Error at %s. Expected key property '%s.%s' but it was missing.",
                    this.getContextString(),
                    this.klass.getName(),
                    dataTypeProperty.getName());
            // TODO: Test nested reference outside projection. For example, question's answer's author is missing a key property
            this.errors.add(error);
        }
    }

    @Override
    protected void handlePlainProperty(@Nonnull DataTypeProperty property)
    {
        if (!property.isRequired())
        {
            return;
        }

        this.handleWarnIfPresent(property, "outside projection");
    }

    @Override
    protected void handleAssociationEndOutsideProjection(AssociationEnd associationEnd)
    {
        if (!associationEnd.isRequired())
        {
            return;
        }

        this.handleWarnIfPresent(associationEnd, "outside projection");
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
