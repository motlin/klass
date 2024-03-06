package cool.klass.deserializer.json;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

public final class JsonTypeCheckingValidator
{
    private final ObjectNode objectNode;
    private final Klass      klass;

    private final MutableStack<String> contextStack = Stacks.mutable.empty();
    private final MutableList<String>  errors;

    public JsonTypeCheckingValidator(ObjectNode objectNode, Klass klass, MutableList<String> errors)
    {
        this.objectNode = objectNode;
        this.klass = klass;
        this.errors = errors;
    }

    public static void validate(ObjectNode objectNode, Klass klass, MutableList<String> errors)
    {
        JsonTypeCheckingValidator incomingDataValidator = new JsonTypeCheckingValidator(
                objectNode,
                klass,
                errors);
        incomingDataValidator.validateIncomingData();
    }

    public void validateIncomingData()
    {
        this.contextStack.push(this.klass.toString());
        this.validateIncomingData(this.objectNode, this.klass);
    }

    private void validateIncomingData(@Nonnull ObjectNode objectNode, @Nonnull Klass klass)
    {
        objectNode.fields().forEachRemaining(entry ->
        {
            String             fieldName        = entry.getKey();
            JsonNode           jsonNode         = entry.getValue();
            Optional<Property> optionalProperty = klass.getPropertyByName(fieldName);

            if (!optionalProperty.isPresent())
            {
                this.handleMissingProperty(klass, fieldName, jsonNode);
                return;
            }

            if (jsonNode.isNull())
            {
                return;
            }

            Property property = optionalProperty.get();
            PropertyVisitor visitor = new JsonTypeCheckingPropertyVisitor(
                    fieldName,
                    jsonNode);
            property.visit(visitor);
        });
    }

    public void handleMissingProperty(@Nonnull Klass klass, String fieldName, JsonNode jsonNode)
    {
        this.contextStack.push(fieldName);

        try
        {
            String error = String.format(
                    "Error at %s. No such property '%s.%s' but got %s. Expected properties: %s.",
                    this.getContextString(),
                    klass,
                    fieldName,
                    jsonNode,
                    klass.getProperties().collect(NamedElement::getName).makeString());
            this.errors.add(error);
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    private final class JsonTypeCheckingPropertyVisitor implements PropertyVisitor
    {
        private final String   fieldName;
        private final JsonNode jsonNode;

        private JsonTypeCheckingPropertyVisitor(
                String fieldName,
                JsonNode jsonNode)
        {
            this.fieldName = fieldName;
            this.jsonNode = jsonNode;
        }

        private void visitPropertyWithContext(@Nonnull Runnable runnable)
        {
            JsonTypeCheckingValidator.this.contextStack.push(this.fieldName);

            try
            {
                runnable.run();
            }
            finally
            {
                JsonTypeCheckingValidator.this.contextStack.pop();
            }
        }

        @Override
        public void visitPrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
        {
            this.visitPropertyWithContext(() -> this.handlePrimitiveProperty(primitiveProperty));
        }

        public void handlePrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
        {
            PrimitiveType primitiveType = primitiveProperty.getType();
            primitiveType.visit(new JsonTypeCheckingPrimitiveTypeVisitor(
                    primitiveProperty.getOwningClassifier(),
                    primitiveProperty,
                    this.jsonNode,
                    JsonTypeCheckingValidator.this.contextStack,
                    JsonTypeCheckingValidator.this.errors));
        }

        @Override
        public void visitEnumerationProperty(@Nonnull EnumerationProperty enumerationProperty)
        {
            this.visitPropertyWithContext(() -> this.handleEnumerationProperty(enumerationProperty));
        }

        public void handleEnumerationProperty(@Nonnull EnumerationProperty enumerationProperty)
        {
            if (!this.jsonNode.isTextual())
            {
                String error = String.format(
                        "Error at %s. Expected enumerated property with type '%s.%s: %s%s' but got %s with type '%s'.",
                        JsonTypeCheckingValidator.this.getContextString(),
                        enumerationProperty.getOwningClassifier().getName(),
                        enumerationProperty.getName(),
                        enumerationProperty.getType().getName(),
                        enumerationProperty.isOptional() ? "?" : "",
                        this.jsonNode,
                        this.jsonNode.getNodeType().toString().toLowerCase());
                JsonTypeCheckingValidator.this.errors.add(error);
            }

            String textValue = this.jsonNode.textValue();

            Enumeration                       enumeration         = enumerationProperty.getType();
            ImmutableList<EnumerationLiteral> enumerationLiterals = enumeration.getEnumerationLiterals();
            if (!enumerationLiterals.asLazy()
                    .collect(EnumerationLiteral::getPrettyName)
                    .contains(textValue))
            {
                ImmutableList<String> quotedPrettyNames = enumerationLiterals
                        .collect(EnumerationLiteral::getPrettyName)
                        .collect(each -> '"' + each + '"');

                String error = String.format(
                        "Error at %s. Expected enumerated property with type '%s.%s: %s%s' but got %s with type '%s'. Expected one of %s.",
                        JsonTypeCheckingValidator.this.getContextString(),
                        enumerationProperty.getOwningClassifier().getName(),
                        enumerationProperty.getName(),
                        enumerationProperty.getType().getName(),
                        enumerationProperty.isOptional() ? "?" : "",
                        this.jsonNode,
                        this.jsonNode.getNodeType().toString().toLowerCase(),
                        quotedPrettyNames.makeString());
                JsonTypeCheckingValidator.this.errors.add(error);
            }
        }

        @Override
        public void visitAssociationEnd(@Nonnull AssociationEnd associationEnd)
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();
            if (multiplicity.isToOne())
            {
                this.visitPropertyWithContext(() -> this.handleToOneAssociationEnd(associationEnd));
                return;
            }

            if (!this.jsonNode.isArray())
            {
                JsonTypeCheckingValidator.this.contextStack.push(this.fieldName);

                try
                {
                    String error = String.format(
                            "Error at %s. Expected json array but value was %s: %s.",
                            JsonTypeCheckingValidator.this.getContextString(),
                            this.jsonNode.getNodeType().toString().toLowerCase(),
                            this.jsonNode);
                    JsonTypeCheckingValidator.this.errors.add(error);
                }
                finally
                {
                    JsonTypeCheckingValidator.this.contextStack.pop();
                }
                return;
            }

            for (int index = 0; index < this.jsonNode.size(); index++)
            {
                String contextString = String.format("%s[%d]", this.fieldName, index);
                JsonTypeCheckingValidator.this.contextStack.push(contextString);

                try
                {
                    JsonNode jsonNode = this.jsonNode.get(index);
                    if (jsonNode instanceof ObjectNode)
                    {
                        JsonTypeCheckingValidator.this.validateIncomingData(
                                (ObjectNode) jsonNode,
                                associationEnd.getType());
                    }
                    else
                    {
                        String error = String.format(
                                "Error at %s. Expected json object but value was %s: %s.",
                                JsonTypeCheckingValidator.this.getContextString(),
                                jsonNode.getNodeType().toString().toLowerCase(),
                                jsonNode);
                        JsonTypeCheckingValidator.this.errors.add(error);
                    }
                }
                finally
                {
                    JsonTypeCheckingValidator.this.contextStack.pop();
                }
            }
        }

        public void handleToOneAssociationEnd(@Nonnull AssociationEnd associationEnd)
        {
            if (this.jsonNode.isObject())
            {
                JsonTypeCheckingValidator.this.validateIncomingData((ObjectNode) this.jsonNode, associationEnd.getType());
            }
            else
            {
                String error = String.format(
                        "Error at %s. Expected json object but value was %s: %s.",
                        JsonTypeCheckingValidator.this.getContextString(),
                        this.jsonNode.getNodeType().toString().toLowerCase(),
                        this.jsonNode);
                JsonTypeCheckingValidator.this.errors.add(error);
            }
        }

        @Override
        public void visitParameterizedProperty(ParameterizedProperty parameterizedProperty)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".visitParameterizedProperty() not implemented yet");
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
