package cool.klass.generator.reladomo.concrete;

import java.time.Instant;
import java.time.LocalDate;

import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;
import org.apache.commons.text.StringEscapeUtils;

@SuppressWarnings("RedundantCast")
public class ValueToJavaSourceCodePrimitiveTypeVisitor
        implements PrimitiveTypeVisitor
{
    private final Object value;

    private String result;

    public ValueToJavaSourceCodePrimitiveTypeVisitor(Object value)
    {
        this.value = value;
    }

    public String getResult()
    {
        return this.result;
    }

    @Override
    public void visitString()
    {
        this.result = "\"" + StringEscapeUtils.escapeJava((String) this.value) + "\"";
    }

    @Override
    public void visitInteger()
    {
        this.result = Integer.toString((Integer) this.value);
    }

    @Override
    public void visitLong()
    {
        this.result = (Long) this.value + "L";
    }

    @Override
    public void visitDouble()
    {
        this.result = Double.toString((Double) this.value);
    }

    @Override
    public void visitFloat()
    {
        this.result = (Float) this.value + "f";
    }

    @Override
    public void visitBoolean()
    {
        this.result = Boolean.toString((Boolean) this.value);
    }

    @Override
    public void visitInstant()
    {
        this.result = "Instant.parse(\"" + ((Instant) this.value) + "\")";
    }

    @Override
    public void visitLocalDate()
    {
        this.result = "LocalDate.parse(\"" + ((LocalDate) this.value) + "\")";
    }

    @Override
    public void visitTemporalInstant()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTemporalInstant() not implemented yet");
    }

    @Override
    public void visitTemporalRange()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTemporalRange() not implemented yet");
    }
}
