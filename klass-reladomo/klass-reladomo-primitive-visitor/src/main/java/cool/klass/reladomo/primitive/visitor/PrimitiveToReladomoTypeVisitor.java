package cool.klass.reladomo.primitive.visitor;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class PrimitiveToReladomoTypeVisitor
        implements PrimitiveTypeVisitor
{
    private String result;

    public static String getJavaType(@Nonnull PrimitiveType primitiveType)
    {
        PrimitiveToReladomoTypeVisitor primitiveToJavaTypeVisitor = new PrimitiveToReladomoTypeVisitor();
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
        this.result = "int";
    }

    @Override
    public void visitLong()
    {
        this.result = "long";
    }

    @Override
    public void visitDouble()
    {
        this.result = "double";
    }

    @Override
    public void visitFloat()
    {
        this.result = "float";
    }

    @Override
    public void visitBoolean()
    {
        this.result = "boolean";
    }

    @Override
    public void visitInstant()
    {
        this.result = "Timestamp";
    }

    @Override
    public void visitLocalDate()
    {
        this.result = "Date";
    }

    @Override
    public void visitTemporalInstant()
    {
        this.result = "Timestamp";
    }

    @Override
    public void visitTemporalRange()
    {
        this.result = "Timestamp";
    }
}
