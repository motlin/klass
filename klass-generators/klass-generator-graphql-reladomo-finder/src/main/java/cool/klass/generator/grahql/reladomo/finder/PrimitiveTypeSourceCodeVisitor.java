package cool.klass.generator.grahql.reladomo.finder;

import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class PrimitiveTypeSourceCodeVisitor
        implements PrimitiveTypeVisitor
{
    private String sourceCode;

    public String getSourceCode()
    {
        return this.sourceCode;
    }

    @Override
    public void visitString()
    {
        this.sourceCode = "String";
    }

    @Override
    public void visitInteger()
    {
        this.sourceCode = "Integer";
    }

    @Override
    public void visitLong()
    {
        this.sourceCode = "Long";
    }

    @Override
    public void visitDouble()
    {
        this.sourceCode = "Double";
    }

    @Override
    public void visitFloat()
    {
        this.sourceCode = "Float";
    }

    @Override
    public void visitBoolean()
    {
        this.sourceCode = "Boolean";
    }

    @Override
    public void visitInstant()
    {
        this.sourceCode = "Timestamp";
    }

    @Override
    public void visitLocalDate()
    {
        this.sourceCode = "Date";
    }

    @Override
    public void visitTemporalInstant()
    {
        this.sourceCode = "Timestamp";
    }

    @Override
    public void visitTemporalRange()
    {
        this.sourceCode = "AsOf";
    }
}
