package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class NamedElement extends Element
{
    @Nonnull
    private final ParserRuleContext nameContext;
    @Nonnull
    private final String            name;
    private final int               ordinal;

    protected NamedElement(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, inferred);
        this.nameContext = Objects.requireNonNull(nameContext);
        this.name = Objects.requireNonNull(name);
        this.ordinal = ordinal;
    }

    @Nonnull
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Nonnull
    public final String getName()
    {
        return this.name;
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }

    public abstract static class NamedElementBuilder extends ElementBuilder
    {
        @Nonnull
        protected final ParserRuleContext nameContext;
        @Nonnull
        protected final String            name;
        protected final int               ordinal;

        protected NamedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, inferred);
            this.nameContext = Objects.requireNonNull(nameContext);
            this.name = Objects.requireNonNull(name);
            this.ordinal = ordinal;
        }

        @Nonnull
        public String getName()
        {
            return this.name;
        }
    }
}
