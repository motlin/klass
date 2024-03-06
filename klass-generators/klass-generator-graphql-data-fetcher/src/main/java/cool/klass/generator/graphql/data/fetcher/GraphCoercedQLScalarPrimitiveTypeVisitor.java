package cool.klass.generator.graphql.data.fetcher;

import java.util.Objects;

import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public final class GraphCoercedQLScalarPrimitiveTypeVisitor
        implements PrimitiveTypeVisitor
{
    private final String propertyName;

    private String sourceCode;

    public GraphCoercedQLScalarPrimitiveTypeVisitor(String propertyName)
    {
        this.propertyName = Objects.requireNonNull(propertyName);
    }

    public String getSourceCode()
    {
        return this.sourceCode;
    }

    @Override
    public void visitString()
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitInteger()
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitLong()
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitDouble()
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitFloat()
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitBoolean()
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitInstant()
    {
        this.sourceCode = String.format(
                "Timestamp.from(%s)",
                this.propertyName);
    }

    @Override
    public void visitLocalDate()
    {
        this.sourceCode = String.format("Timestamp.valueOf(%s.atStartOfDay())", this.propertyName);
    }

    @Override
    public void visitTemporalInstant()
    {
        this.sourceCode = String.format(
                "Timestamp.valueOf(LocalDateTime.ofInstant(%s, ZoneOffset.UTC))",
                this.propertyName);
    }

    @Override
    public void visitTemporalRange()
    {
        this.sourceCode = String.format(
                "Timestamp.valueOf(LocalDateTime.ofInstant(%s, ZoneOffset.UTC))",
                this.propertyName);
    }
}
