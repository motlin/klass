package cool.klass.model.meta.domain.api.property;

import cool.klass.model.meta.domain.api.visitor.DataTypePropertyVisitor;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class DataTypePropertyVisitorAdaptor implements PrimitiveTypeVisitor
{
    private final DataTypePropertyVisitor visitor;
    private final PrimitiveProperty       primitiveProperty;

    public DataTypePropertyVisitorAdaptor(
            DataTypePropertyVisitor visitor,
            PrimitiveProperty primitiveProperty)
    {
        this.visitor           = visitor;
        this.primitiveProperty = primitiveProperty;
    }

    @Override
    public void visitString()
    {
        this.visitor.visitString(this.primitiveProperty);
    }

    @Override
    public void visitInteger()
    {
        this.visitor.visitInteger(this.primitiveProperty);
    }

    @Override
    public void visitLong()
    {
        this.visitor.visitLong(this.primitiveProperty);
    }

    @Override
    public void visitDouble()
    {
        this.visitor.visitDouble(this.primitiveProperty);
    }

    @Override
    public void visitFloat()
    {
        this.visitor.visitFloat(this.primitiveProperty);
    }

    @Override
    public void visitBoolean()
    {
        this.visitor.visitBoolean(this.primitiveProperty);
    }

    @Override
    public void visitInstant()
    {
        this.visitor.visitInstant(this.primitiveProperty);
    }

    @Override
    public void visitLocalDate()
    {
        this.visitor.visitLocalDate(this.primitiveProperty);
    }

    @Override
    public void visitTemporalInstant()
    {
        this.visitor.visitTemporalInstant(this.primitiveProperty);
    }

    @Override
    public void visitTemporalRange()
    {
        this.visitor.visitTemporalRange(this.primitiveProperty);
    }
}
