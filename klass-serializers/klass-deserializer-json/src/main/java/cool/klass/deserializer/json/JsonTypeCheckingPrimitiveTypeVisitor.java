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
    private final JsonNode             jsonNode;
    private final MutableStack<String> contextStack;
    private final MutableList<String>  errors;

    public JsonTypeCheckingPrimitiveTypeVisitor(
            Classifier classifier,
            PrimitiveProperty primitiveProperty,
            JsonNode jsonNode,
            MutableStack<String> contextStack,
            MutableList<String> errors)
    {
        this.classifier = Objects.requireNonNull(classifier);
        this.primitiveProperty = Objects.requireNonNull(primitiveProperty);
        this.jsonNode = Objects.requireNonNull(jsonNode);
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
                this.jsonNode,
                this.jsonNode.getNodeType().toString().toLowerCase());
        this.errors.add(error);
    }

    // TODO: Test nullable primitives

    @Override
    public void visitString()
    {
        if (!this.jsonNode.isTextual())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitInteger()
    {
        if (!this.jsonNode.isIntegralNumber() || !this.jsonNode.canConvertToInt())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitLong()
    {
        if (!this.jsonNode.isIntegralNumber() || !this.jsonNode.canConvertToLong())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitDouble()
    {
        if (!this.jsonNode.isDouble()
                && !this.jsonNode.isFloat()
                && !this.jsonNode.isInt()
                && !this.jsonNode.isLong())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitFloat()
    {
        if (!this.jsonNode.isDouble()
                && !this.jsonNode.isFloat()
                && !this.jsonNode.isInt()
                && !this.jsonNode.isLong()
                || this.jsonNode.doubleValue() != this.jsonNode.floatValue())
        {
            this.emitTypeError();
        }
    }

    @Override
    public void visitBoolean()
    {
        if (!this.jsonNode.isBoolean())
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
        if (!this.jsonNode.isTextual())
        {
            this.emitTypeError();
        }

        String text = this.jsonNode.textValue();
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
                    this.jsonNode);
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
        if (this.jsonNode.isNull()
                && this.primitiveProperty.isTemporalInstant()
                && this.primitiveProperty.getPropertyModifiers().anySatisfy(PropertyModifier::isTo))
        {
            // TODO: Other validations might make this one unreachable
            return;
        }

        if (!this.jsonNode.isTextual())
        {
            this.emitTypeError();
            return;
        }

        String text = this.jsonNode.textValue();
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
                    this.jsonNode);
            this.errors.add(error);
        }
    }
}
