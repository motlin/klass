package cool.klass.reladomo.test.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.visitor.DataTypePropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoRequiredDataTypePropertyVisitor implements DataTypePropertyVisitor
{
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(1999, 12, 31, 23, 59);
    public static final Instant       INSTANT         = LOCAL_DATE_TIME.toInstant(ZoneOffset.UTC);

    private Object result;

    public Object getResult()
    {
        return this.result;
    }

    @Override
    public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        ImmutableList<EnumerationLiteral> enumerationLiterals = enumerationProperty.getType().getEnumerationLiterals();
        // TODO: Compiler error for enumeration with <2 literals
        this.result = enumerationLiterals.get(0);
    }

    @Override
    public void visitString(PrimitiveProperty primitiveProperty)
    {
        this.result = String.format(
                "%s %s 1 ☝",
                primitiveProperty.getOwningKlass().getName(),
                primitiveProperty.getName());
    }

    @Override
    public void visitInteger(PrimitiveProperty primitiveProperty)
    {
        this.result = 1;
    }

    @Override
    public void visitLong(PrimitiveProperty primitiveProperty)
    {
        this.result = 100_000_000_000L;
    }

    @Override
    public void visitDouble(PrimitiveProperty primitiveProperty)
    {
        this.result = 1.0123456789;
    }

    @Override
    public void visitFloat(PrimitiveProperty primitiveProperty)
    {
        this.result = 1.01234567f;
    }

    @Override
    public void visitBoolean(PrimitiveProperty primitiveProperty)
    {
        this.result = true;
    }

    @Override
    public void visitInstant(PrimitiveProperty primitiveProperty)
    {
        this.result = INSTANT;
    }

    @Override
    public void visitLocalDate(PrimitiveProperty primitiveProperty)
    {
        this.result = LOCAL_DATE_TIME;
    }

    @Override
    public void visitTemporalInstant(PrimitiveProperty primitiveProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTemporalInstant() not implemented yet");
    }

    @Override
    public void visitTemporalRange(PrimitiveProperty primitiveProperty)
    {
        if (!primitiveProperty.isSystem())
        {
            throw new AssertionError();
        }
    }
}
