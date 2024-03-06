package cool.klass.reladomo.persistent.writer;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.AssertValuesMatchPropertyVisitor;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class IncomingCreatedByDataModelValidator
{
    @Nonnull
    protected final DataStore                dataStore;
    @Nonnull
    protected final Klass                    userKlass;
    @Nonnull
    protected final Klass                    klass;
    @Nonnull
    protected final MutationContext          mutationContext;
    @Nonnull
    protected final Object                   persistentInstance;
    @Nonnull
    protected final ObjectNode               objectNode;
    @Nonnull
    protected final MutableList<String>      errors;
    @Nonnull
    protected final MutableList<String>      warnings;
    @Nonnull
    protected final MutableStack<String>     contextStack;
    @Nonnull
    protected final Optional<AssociationEnd> pathHere;

    public IncomingCreatedByDataModelValidator(
            @Nonnull DataStore dataStore,
            @Nonnull Klass userKlass,
            @Nonnull Klass klass,
            @Nonnull MutationContext mutationContext,
            @Nonnull Object persistentInstance,
            @Nonnull ObjectNode objectNode,
            @Nonnull MutableList<String> errors,
            @Nonnull MutableList<String> warnings,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull Optional<AssociationEnd> pathHere)
    {
        this.dataStore          = Objects.requireNonNull(dataStore);
        this.userKlass          = Objects.requireNonNull(userKlass);
        this.klass              = Objects.requireNonNull(klass);
        this.mutationContext    = Objects.requireNonNull(mutationContext);
        this.persistentInstance = Objects.requireNonNull(persistentInstance);
        this.objectNode         = Objects.requireNonNull(objectNode);
        this.errors             = Objects.requireNonNull(errors);
        this.warnings           = Objects.requireNonNull(warnings);
        this.contextStack       = Objects.requireNonNull(contextStack);
        this.pathHere           = Objects.requireNonNull(pathHere);
    }

    public void validate()
    {
        this.handleDataTypePropertiesInsideProjection();
        this.handleAssociationEnds();
    }

    //region DataTypeProperties
    private void handleDataTypePropertiesInsideProjection()
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = this.klass.getDataTypeProperties();
        for (DataTypeProperty dataTypeProperty : dataTypeProperties)
        {
            this.handleDataTypePropertyInsideProjection(dataTypeProperty);
        }
    }

    private void handleDataTypePropertyInsideProjection(@Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isKey())
        {
            this.handleAuditProperty(dataTypeProperty);
        }
        if (dataTypeProperty.isTemporalInstant())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "temporal");
            return;
        }
        if (dataTypeProperty.isCreatedBy() || dataTypeProperty.isLastUpdatedBy())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "user id");
            return;
        }
        if (dataTypeProperty.isCreatedOn())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "created on");
            return;
        }
        if (dataTypeProperty.isVersion())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "version");
            return;
        }
        if (dataTypeProperty.isFinal())
        {
            this.checkPropertyMatchesIfPresent(dataTypeProperty, "final");
        }
    }

    private void handleAuditProperty(@Nonnull DataTypeProperty dataTypeProperty)
    {
        this.contextStack.push(dataTypeProperty.getName());

        try
        {
            JsonNode jsonDataTypeValue = this.objectNode.path(dataTypeProperty.getName());
            if (jsonDataTypeValue.isMissingNode() || !jsonDataTypeValue.isTextual())
            {
                return;
            }

            Optional<String> maybeUserId = this.mutationContext.getUserId();
            if (maybeUserId.isEmpty())
            {
                return;
            }

            if (maybeUserId.get().equals(jsonDataTypeValue.asText()))
            {
                return;
            }

            String warning = "Warning at %s. Expected audit property '%s' to match current user '%s' but got '%s'."
                    .formatted(
                            this.getContextString(),
                            dataTypeProperty.getName(),
                            maybeUserId.get(),
                            jsonDataTypeValue.asText());
            this.warnings.add(warning);
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    private void checkPropertyMatchesIfPresent(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull String propertyKind)
    {
        this.contextStack.push(dataTypeProperty.getName());

        try
        {
            this.checkPropertyMatches(dataTypeProperty, propertyKind);
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    private void checkPropertyMatches(@Nonnull DataTypeProperty property, @Nonnull String propertyKind)
    {
        JsonNode jsonDataTypeValue = this.objectNode.path(property.getName());
        if (jsonDataTypeValue.isMissingNode())
        {
            return;
        }

        Object persistentValue = this.dataStore.getDataTypeProperty(this.persistentInstance, property);
        PropertyVisitor visitor = new AssertValuesMatchPropertyVisitor(
                jsonDataTypeValue,
                persistentValue,
                propertyKind,
                this.contextStack,
                "Warning",
                this.warnings);
        property.visit(visitor);
    }

    public String getContextString()
    {
        return this.contextStack
                .toList()
                .asReversed()
                .makeString(".");
    }
    //endregion

    //region AssociationEnds
    private void handleAssociationEnds()
    {
        for (AssociationEnd associationEnd : this.klass.getAssociationEnds())
        {
            this.handleAssociationEnd(associationEnd);
        }
    }

    private void handleAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        this.handleWarnIfPresent(associationEnd, "user type");
    }

    private void handleWarnIfPresent(@Nonnull AssociationEnd property, String propertyKind)
    {
        JsonNode jsonNode = this.objectNode.path(property.getName());
        if (jsonNode.isMissingNode())
        {
            return;
        }

        String jsonNodeString = jsonNode.isNull() ? "" : ": " + jsonNode;
        String warning = String.format(
                "Warning at %s. Didn't expect to receive value for %s association end '%s.%s: %s[%s]' but value was %s%s.",
                this.getContextString(),
                propertyKind,
                property.getOwningClassifier().getName(),
                property.getName(),
                property.getType(),
                property.getMultiplicity().getPrettyName(),
                jsonNode.getNodeType().toString().toLowerCase(),
                jsonNodeString);
        this.warnings.add(warning);
    }
}
