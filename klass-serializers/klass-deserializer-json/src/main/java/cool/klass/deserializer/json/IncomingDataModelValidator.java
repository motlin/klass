package cool.klass.deserializer.json;

import java.util.Optional;

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

public final class IncomingDataModelValidator
{
    private final ObjectNode objectNode;
    private final Klass      klass;

    private final MutableStack<String> contextStack = Stacks.mutable.empty();
    private final MutableList<String>  errors;

    public IncomingDataModelValidator(ObjectNode objectNode, Klass klass, MutableList<String> errors)
    {
        this.objectNode = objectNode;
        this.klass = klass;
        this.errors = errors;
    }

    public static void validate(ObjectNode objectNode, Klass klass, MutableList<String> errors)
    {
        IncomingDataModelValidator incomingDataValidator = new IncomingDataModelValidator(
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

    private void validateIncomingData(ObjectNode objectNode, Klass klass)
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

            Property property = optionalProperty.get();
            property.visit(new IncomingDataValidatorPropertyVisitor(fieldName, jsonNode));
        });
    }

    public void handleMissingProperty(Klass klass, String fieldName, JsonNode jsonNode)
    {
        this.contextStack.push(fieldName);

        try
        {
            String error = String.format(
                    "Error at %s. No such property '%s' on type %s but got %s. Expected properties: %s.",
                    this.getContextString(),
                    fieldName,
                    klass,
                    jsonNode,
                    klass.getProperties().collect(NamedElement::getName).makeString());
            this.errors.add(error);
        }
        finally
        {
            this.contextStack.pop();
        }
    }

    private final class IncomingDataValidatorPropertyVisitor implements PropertyVisitor
    {
        private final String   fieldName;
        private final JsonNode jsonNode;

        private IncomingDataValidatorPropertyVisitor(
                String fieldName,
                JsonNode jsonNode)
        {
            this.fieldName = fieldName;
            this.jsonNode = jsonNode;
        }

        private void visitPropertyWithContext(Runnable runnable)
        {
            IncomingDataModelValidator.this.contextStack.push(this.fieldName);

            try
            {
                runnable.run();
            }
            finally
            {
                IncomingDataModelValidator.this.contextStack.pop();
            }
        }

        @Override
        public void visitPrimitiveProperty(PrimitiveProperty primitiveProperty)
        {
            this.visitPropertyWithContext(() -> this.handlePrimitiveProperty(primitiveProperty));
        }

        public void handlePrimitiveProperty(PrimitiveProperty primitiveProperty)
        {
            PrimitiveType primitiveType = primitiveProperty.getType();
            primitiveType.visit(new ValidateIncomingDataPrimitiveTypeVisitor(
                    primitiveProperty.getOwningKlass(),
                    primitiveProperty,
                    this.jsonNode,
                    IncomingDataModelValidator.this.contextStack,
                    IncomingDataModelValidator.this.errors));
        }

        @Override
        public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
        {
            this.visitPropertyWithContext(() -> this.handleEnumerationProperty(enumerationProperty));
        }

        public void handleEnumerationProperty(EnumerationProperty enumerationProperty)
        {
            if (!this.jsonNode.isTextual())
            {
                String error = String.format(
                        "Incoming '%s' has property '%s' but got '%s'. Context: %s.",
                        enumerationProperty.getOwningKlass(),
                        enumerationProperty,
                        this.jsonNode,
                        IncomingDataModelValidator.this.getContextString());
                IncomingDataModelValidator.this.errors.add(error);
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
                        IncomingDataModelValidator.this.getContextString(),
                        enumerationProperty.getOwningKlass().getName(),
                        enumerationProperty.getName(),
                        enumerationProperty.getType().getName(),
                        enumerationProperty.isOptional() ? "?" : "",
                        this.jsonNode,
                        this.jsonNode.getNodeType().toString().toLowerCase(),
                        quotedPrettyNames.makeString());
                IncomingDataModelValidator.this.errors.add(error);
            }
        }

        @Override
        public void visitAssociationEnd(AssociationEnd associationEnd)
        {
            Multiplicity multiplicity = associationEnd.getMultiplicity();
            if (multiplicity.isToOne())
            {
                this.visitPropertyWithContext(() -> this.handleToOneAssociationEnd(associationEnd));
            }
            else
            {
                if (this.jsonNode.isArray())
                {
                    for (int index = 0; index < this.jsonNode.size(); index++)
                    {
                        String contextString = String.format("%s[%d]", this.fieldName, index);
                        IncomingDataModelValidator.this.contextStack.push(contextString);

                        try
                        {
                            JsonNode jsonNode = this.jsonNode.get(index);
                            if (jsonNode instanceof ObjectNode)
                            {
                                IncomingDataModelValidator.this.validateIncomingData(
                                        (ObjectNode) jsonNode,
                                        associationEnd.getType());
                            }
                            else
                            {
                                IncomingDataModelValidator.this.errors.add("TODO");
                            }
                        }
                        finally
                        {
                            IncomingDataModelValidator.this.contextStack.pop();
                        }
                    }
                }
                else
                {
                    IncomingDataModelValidator.this.contextStack.push(this.fieldName);

                    try
                    {
                        IncomingDataModelValidator.this.errors.add("TODO");
                    }
                    finally
                    {
                        IncomingDataModelValidator.this.contextStack.pop();
                    }
                }
            }
        }

        public void handleToOneAssociationEnd(AssociationEnd associationEnd)
        {
            if (this.jsonNode.isObject())
            {
                IncomingDataModelValidator.this.validateIncomingData((ObjectNode) this.jsonNode, associationEnd.getType());
            }
            else
            {
                IncomingDataModelValidator.this.errors.add("TODO");
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
