package cool.klass.generator.dto;

import cool.klass.model.meta.domain.visitor.PrimitiveTypeVisitor;

public class PrimitiveToJavaTypeVisitor implements PrimitiveTypeVisitor
{
    private String result;

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
        this.result = "Instant";
    }

    @Override
    public void visitLocalDate()
    {
        this.result = "LocalDate";
    }

    @Override
    public void visitTemporalInstant()
    {
        this.result = "Instant";
    }

    @Override
    public void visitTemporalRange()
    {
        this.result = "Instant";
    }
}
