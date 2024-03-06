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

    protected NamedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name)
    {
        super(elementContext);
        this.nameContext = Objects.requireNonNull(nameContext);
        this.name = Objects.requireNonNull(name);
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

    public abstract static class NamedElementBuilder extends ElementBuilder
    {
        @Nonnull
        protected final ParserRuleContext nameContext;
        @Nonnull
        protected final String            name;

        protected NamedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name)
        {
            super(elementContext);
            this.nameContext = Objects.requireNonNull(nameContext);
            this.name = Objects.requireNonNull(name);
        }

        @Nonnull
        public String getName()
        {
            return this.name;
        }
    }
}
