package cool.klass.reladomo.test.data;

import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.visitor.DataTypePropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoOptionalDataTypePropertyVisitor implements DataTypePropertyVisitor
{
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
        this.result = enumerationLiterals.get(1);
    }

    @Override
    public void visitString(PrimitiveProperty primitiveProperty)
    {
        this.result = primitiveProperty.getName() + " 2 ✌️";
    }

    @Override
    public void visitInteger(PrimitiveProperty primitiveProperty)
    {
        this.result = 2;
    }

    @Override
    public void visitLong(PrimitiveProperty primitiveProperty)
    {
        this.result = 2L;
    }

    @Override
    public void visitDouble(PrimitiveProperty primitiveProperty)
    {
        this.result = 2.0;
    }

    @Override
    public void visitFloat(PrimitiveProperty primitiveProperty)
    {
        this.result = 2.0f;
    }

    @Override
    public void visitBoolean(PrimitiveProperty primitiveProperty)
    {
        this.result = false;
    }

    @Override
    public void visitInstant(PrimitiveProperty primitiveProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitInstant() not implemented yet");
    }

    @Override
    public void visitLocalDate(PrimitiveProperty primitiveProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitLocalDate() not implemented yet");
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
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTemporalRange() not implemented yet");
    }
}
