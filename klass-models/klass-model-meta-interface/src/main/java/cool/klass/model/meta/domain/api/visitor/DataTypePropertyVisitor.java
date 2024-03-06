package cool.klass.model.meta.domain.api.visitor;

import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;

public interface DataTypePropertyVisitor
{
    void visitEnumerationProperty(EnumerationProperty enumerationProperty);

    void visitString(PrimitiveProperty primitiveProperty);

    void visitInteger(PrimitiveProperty primitiveProperty);

    void visitLong(PrimitiveProperty primitiveProperty);

    void visitDouble(PrimitiveProperty primitiveProperty);

    void visitFloat(PrimitiveProperty primitiveProperty);

    void visitBoolean(PrimitiveProperty primitiveProperty);

    void visitInstant(PrimitiveProperty primitiveProperty);

    void visitLocalDate(PrimitiveProperty primitiveProperty);

    void visitTemporalInstant(PrimitiveProperty primitiveProperty);

    void visitTemporalRange(PrimitiveProperty primitiveProperty);
}
