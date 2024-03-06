package cool.klass.generator.service;

import java.util.Objects;

import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class PrimitiveSetVisitor implements PrimitiveTypeVisitor
{
    private final StringBuilder stringBuilder;
    private final String        parameterName;

    public PrimitiveSetVisitor(StringBuilder stringBuilder, String parameterName)
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
        this.stringBuilder.append("SetAdapter.adapt(");
        this.stringBuilder.append(this.parameterName);
        this.stringBuilder.append(").collectInt(x -> x, IntSets.mutable.empty())");
    }

    @Override
    public void visitLong()
    {
        this.stringBuilder.append("SetAdapter.adapt(");
        this.stringBuilder.append(this.parameterName);
        this.stringBuilder.append(").collectLong(x -> x, LongSets.mutable.empty())");
    }

    @Override
    public void visitDouble()
    {
        this.stringBuilder.append("SetAdapter.adapt(");
        this.stringBuilder.append(this.parameterName);
        this.stringBuilder.append(").collectDouble(x -> x, DoubleSets.mutable.empty())");
    }

    @Override
    public void visitFloat()
    {
        this.stringBuilder.append("SetAdapter.adapt(");
        this.stringBuilder.append(this.parameterName);
        this.stringBuilder.append(").collectFloat(x -> x, FloatSets.mutable.empty())");
    }

    @Override
    public void visitBoolean()
    {
        this.stringBuilder.append("SetAdapter.adapt(");
        this.stringBuilder.append(this.parameterName);
        this.stringBuilder.append(").collectBoolean(x -> x, BooleanSets.mutable.empty())");
    }

    @Override
    public void visitInstant()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitLocalDate()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitTemporalInstant()
    {
        this.stringBuilder.append(this.parameterName);
    }

    @Override
    public void visitTemporalRange()
    {
        this.stringBuilder.append(this.parameterName);
    }
}
