package cool.klass.model.meta.domain.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Parameter extends NamedElement
{
    @Nonnull
    protected final Multiplicity   multiplicity;

    protected Parameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    @Nonnull
    public abstract DataType getType();

    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Override
    public String toString()
    {
        return String.format("{%s}", this.getName());
    }

    public abstract static class ParameterBuilder extends NamedElementBuilder
    {
        @Nonnull
        protected final Multiplicity multiplicity;

        public ParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.multiplicity = Objects.requireNonNull(multiplicity);
        }

        @Nonnull
        public abstract Parameter build();

        @Nonnull
        public abstract Parameter getParameter();
    }
}
