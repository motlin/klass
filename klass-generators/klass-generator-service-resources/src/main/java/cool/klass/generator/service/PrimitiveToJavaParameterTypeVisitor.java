package cool.klass.generator.service;

import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class PrimitiveToJavaParameterTypeVisitor implements PrimitiveTypeVisitor
{
    private String result;

    public static String getJavaType(PrimitiveType primitiveType)
    {
        PrimitiveToJavaParameterTypeVisitor primitiveToJavaTypeVisitor = new PrimitiveToJavaParameterTypeVisitor();
        primitiveType.visit(primitiveToJavaTypeVisitor);
        return primitiveToJavaTypeVisitor.getResult();
    }

    public String getResult()
    {
        return this.result;
    }

    @Override
    public void visitString()
    {
        this.result = "String";
    }

    @Override
    public void visitInteger()
    {
        this.result = "Integer";
    }

    @Override
    public void visitLong()
    {
        this.result = "Long";
    }

    @Override
    public void visitDouble()
    {
        this.result = "Double";
    }

    @Override
    public void visitFloat()
    {
        this.result = "Float";
    }

    @Override
    public void visitBoolean()
    {
        this.result = "Boolean";
    }

    @Override
    public void visitInstant()
    {
        this.result = "InstantParam";
    }

    @Override
    public void visitLocalDate()
    {
        this.result = "LocalDateParam";
    }

    @Override
    public void visitTemporalInstant()
    {
        this.result = "InstantParam";
    }

    @Override
    public void visitTemporalRange()
    {
        this.result = "InstantParam";
    }
}
