package cool.klass.serializer.json;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonGenerator;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class SerializeValueToJsonFieldPrimitiveTypeVisitor implements PrimitiveTypeVisitor
{
    private final JsonGenerator jsonGenerator;
    private final String        propertyName;
    private final Object        value;

    public SerializeValueToJsonFieldPrimitiveTypeVisitor(
            JsonGenerator jsonGenerator,
            String primitivePropertyName,
            Object value)
    {
        this.jsonGenerator = Objects.requireNonNull(jsonGenerator);
        this.propertyName = Objects.requireNonNull(primitivePropertyName);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public void visitString() throws IOException
    {
        this.jsonGenerator.writeStringField(this.propertyName, (String) this.value);
    }

    @Override
    public void visitInteger() throws IOException
    {
        this.jsonGenerator.writeNumberField(this.propertyName, (Integer) this.value);
    }

    @Override
    public void visitLong() throws IOException
    {
        this.jsonGenerator.writeNumberField(this.propertyName, (Long) this.value);
    }

    @Override
    public void visitDouble() throws IOException
    {
        this.jsonGenerator.writeNumberField(this.propertyName, (Double) this.value);
    }

    @Override
    public void visitFloat() throws IOException
    {
        this.jsonGenerator.writeNumberField(this.propertyName, (Float) this.value);
    }

    @Override
    public void visitBoolean() throws IOException
    {
        this.jsonGenerator.writeBooleanField(this.propertyName, (Boolean) this.value);
    }

    @Override
    public void visitInstant() throws IOException
    {
        this.visitString();
    }

    @Override
    public void visitLocalDate() throws IOException
    {
        this.jsonGenerator.writeStringField(this.propertyName, this.value.toString());
    }

    @Override
    public void visitTemporalInstant() throws IOException
    {
        this.visitString();
    }

    @Override
    public void visitTemporalRange()
    {
        throw new IllegalStateException();
    }
}
