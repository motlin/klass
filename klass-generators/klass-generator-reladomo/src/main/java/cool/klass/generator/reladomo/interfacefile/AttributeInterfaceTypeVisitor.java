package cool.klass.generator.reladomo.interfacefile;

import com.gs.fw.common.mithra.generator.metamodel.AttributeInterfaceType;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

class AttributeInterfaceTypeVisitor implements PrimitiveTypeVisitor
{
    private final AttributeInterfaceType attributeType;

    AttributeInterfaceTypeVisitor(AttributeInterfaceType attributeType)
    {
        this.attributeType = attributeType;
    }

    @Override
    public void visitString()
    {
        this.attributeType.setJavaType("String");
    }

    @Override
    public void visitInteger()
    {
        this.attributeType.setJavaType("int");
    }

    @Override
    public void visitLong()
    {
        this.attributeType.setJavaType("long");
    }

    @Override
    public void visitDouble()
    {
        this.attributeType.setJavaType("double");
    }

    @Override
    public void visitFloat()
    {
        this.attributeType.setJavaType("float");
    }

    @Override
    public void visitBoolean()
    {
        this.attributeType.setJavaType("boolean");
    }

    @Override
    public void visitInstant()
    {
        this.attributeType.setJavaType("Timestamp");
    }

    @Override
    public void visitLocalDate()
    {
        this.attributeType.setJavaType("Date");
    }

    @Override
    public void visitTemporalInstant()
    {
        throw new AssertionError();
    }

    @Override
    public void visitTemporalRange()
    {
        throw new AssertionError();
    }
}
