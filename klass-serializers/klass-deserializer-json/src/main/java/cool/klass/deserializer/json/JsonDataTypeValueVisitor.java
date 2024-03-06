package cool.klass.deserializer.json;

import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;

public class JsonDataTypeValueVisitor implements PropertyVisitor
{
    private final JsonNode jsonDataTypeValue;

    private Object result;

    public JsonDataTypeValueVisitor(JsonNode jsonDataTypeValue)
    {
        this.jsonDataTypeValue = jsonDataTypeValue;
    }

    // TODO: Needs temporal context
    public static Object extractDataTypePropertyFromJson(DataTypeProperty dataTypeProperty, ObjectNode incomingJson)
    {
        JsonNode jsonDataTypeValue = incomingJson.path(dataTypeProperty.getName());
        if (jsonDataTypeValue.isMissingNode() || jsonDataTypeValue.isNull())
        {
            return null;
        }

        JsonDataTypeValueVisitor visitor = new JsonDataTypeValueVisitor(jsonDataTypeValue);
        dataTypeProperty.visit(visitor);
        return visitor.getResult();
    }

    public Object getResult()
    {
        return this.result;
    }

    @Override
    public void visitPrimitiveProperty(PrimitiveProperty primitiveProperty)
    {
        JsonPrimitiveTypeValueVisitor visitor = new JsonPrimitiveTypeValueVisitor(this.jsonDataTypeValue);
        primitiveProperty.getType().visit(visitor);
        this.result = visitor.getResult();
    }

    @Override
    public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        if (!this.jsonDataTypeValue.isTextual())
        {
            throw new AssertionError();
        }
        String textValue = this.jsonDataTypeValue.textValue();
        Optional<EnumerationLiteral> enumerationLiteral = enumerationProperty.getType().getEnumerationLiterals()
                .detectOptional(each -> each.getPrettyName().equals(textValue));
        this.result = enumerationLiteral.get();
    }

    @Override
    public void visitAssociationEnd(AssociationEnd associationEnd)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitAssociationEnd() not implemented yet");
    }

    @Override
    public void visitParameterizedProperty(ParameterizedProperty parameterizedProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitParameterizedProperty() not implemented yet");
    }
}
