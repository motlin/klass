package cool.klass.generator.service;

import java.util.Objects;

import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class PrimitiveVisitor implements PrimitiveTypeVisitor
{
    private final StringBuilder stringBuilder;
    private final String        parameterName;

    public PrimitiveVisitor(StringBuilder stringBuilder, String parameterName)
    {
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
        this.parameterName = Objects.requireNonNull(parameterName);
    }

    @Override
    public void visitString()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitInteger()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitLong()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitDouble()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitFloat()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitBoolean()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitInstant()
    {
        String sourceCode = String.format(
                "Timestamp.from(%s.get().toZonedDateTime().toInstant())",
                this.parameterName);
        this.stringBuilder.append(sourceCode);
    }

    @Override
    public void visitLocalDate()
    {
        String sourceCode = String.format("Timestamp.valueOf(%s.get().atStartOfDay())", this.parameterName);
        this.stringBuilder.append(sourceCode);
    }

    @Override
    public void visitTemporalInstant()
    {
        // TODO: Coverage
        String sourceCode = String.format(
                "Timestamp.valueOf(LocalDateTime.ofInstant((Instant) %s, ZoneOffset.UTC))",
                this.parameterName);
        this.stringBuilder.append(sourceCode);
    }

    @Override
    public void visitTemporalRange()
    {
        // TODO: Coverage
        String sourceCode = String.format(
                "Timestamp.valueOf(LocalDateTime.ofInstant((Instant) %s, ZoneOffset.UTC))",
                this.parameterName);
        this.stringBuilder.append(sourceCode);
    }
}
