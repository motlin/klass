package cool.klass.deserializer.json;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class JsonTypeCheckingPrimitiveTypeVisitor implements PrimitiveTypeVisitor
{
    private final Classifier           classifier;
    private final PrimitiveProperty    primitiveProperty;
    private final JsonNode             jsonDataTypeValue;
    private final MutableStack<String> contextStack;
    private final MutableList<String>  errors;

    public JsonTypeCheckingPrimitiveTypeVisitor(
            Classifier classifier,
            PrimitiveProperty primitiveProperty,
            JsonNode jsonDataTypeValue,
            MutableStack<String> contextStack,
            MutableList<String> errors)
    {
        this.classifier = Objects.requireNonNull(classifier);
        this.primitiveProperty = Objects.requireNonNull(primitiveProperty);
        this.jsonDataTypeValue = Objects.requireNonNull(jsonDataTypeValue);
        this.contextStack = Objects.requireNonNull(contextStack);
        this.errors = Objects.requireNonNull(errors);
    }

    private void emitTypeError()
    {
        String contextString = this.contextStack.toList().asReversed().makeString(".");
        String error = String.format(
                "Error at %s. Expected property with type '%s.%s: %s%s' but got '%s' with type '%s'.",
                contextString,
                this.primitiveProperty.getOwningClassifier().getName(),
                this.primitiveProperty.getName(),
                this.primitiveProperty.getType().getPrettyName(),
                this.primitiveProperty.isOptional() ? "?" : "",
                this.jsonDataTypeValue,
                this.jsonDataTypeValue.getNodeType().toString().toLowerCase());
        this.errors.add(error);
    }

    // TODO: Test nullable primitives

    @Override
    public void visitString()
    {
        if (!this.jsonDataTypeValue.isTextual())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitInteger()
    {
        if (!this.jsonDataTypeValue.isIntegralNumber() || !this.jsonDataTypeValue.canConvertToInt())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitLong()
    {
        if (!this.jsonDataTypeValue.isIntegralNumber() || !this.jsonDataTypeValue.canConvertToLong())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitDouble()
    {
        if (!this.jsonDataTypeValue.isDouble()
                && !this.jsonDataTypeValue.isFloat()
                && !this.jsonDataTypeValue.isInt()
                && !this.jsonDataTypeValue.isLong())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitFloat()
    {
        if (!this.jsonDataTypeValue.isDouble()
                && !this.jsonDataTypeValue.isFloat()
                && !this.jsonDataTypeValue.isInt()
                && !this.jsonDataTypeValue.isLong()
                || !this.hasValidFloatString())
        {
            this.emitTypeError();
        }
    }

    private boolean hasValidFloatString()
    {
        double doubleValue  = this.jsonDataTypeValue.doubleValue();
        float  floatValue   = this.jsonDataTypeValue.floatValue();
        String doubleString = Double.toString(doubleValue);
        String floatString  = Float.toString(floatValue);
        return doubleString.equals(floatString);
    }

    @Override
    public void visitBoolean()
    {
        if (!this.jsonDataTypeValue.isBoolean())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitInstant()
    {
        this.visitTemporal();
    }

    @Override
    public void visitLocalDate()
    {
        if (!this.jsonDataTypeValue.isTextual())
        {
            this.emitTypeError();
            return;
        }

        String text = this.jsonDataTypeValue.textValue();
        if (text.equals("now") || text.equals("infinity"))
        {
            return;
        }

        try
        {
            LocalDate.parse(text);
        }
        catch (DateTimeParseException e)
        {
            String error = String.format(
                    "Incoming '%s' has property '%s' but got '%s'. Could not be parsed by LocalDate.parse().",
                    this.classifier,
                    this.primitiveProperty,
                    this.jsonDataTypeValue);
            this.errors.add(error);
        }
    }

    @Override
    public void visitTemporalInstant()
    {
        this.visitTemporal();
    }

    @Override
    public void visitTemporalRange()
    {
        this.visitTemporal();
    }

    private void visitTemporal()
    {
        if (this.jsonDataTypeValue.isNull()
                && this.primitiveProperty.isTemporalInstant()
                && this.primitiveProperty.getPropertyModifiers().anySatisfy(PropertyModifier::isTo))
        {
            // TODO: Other validations might make this one unreachable
            return;
        }

        if (!this.jsonDataTypeValue.isTextual())
        {
            this.emitTypeError();
            return;
        }

        String text = this.jsonDataTypeValue.textValue();
        if (text.equals("now") || text.equals("infinity"))
        {
            return;
        }

        try
        {
            Instant.parse(text);
        }
        catch (DateTimeParseException e)
        {
            String error = String.format(
                    "Incoming '%s' has property '%s' but got '%s'. Could not be parsed by java.time.format.DateTimeFormatter.ISO_INSTANT which expects a String like '1999-12-31T23:59:59Z'",
                    this.classifier,
                    this.primitiveProperty,
                    this.jsonDataTypeValue);
            this.errors.add(error);
        }
    }
}
