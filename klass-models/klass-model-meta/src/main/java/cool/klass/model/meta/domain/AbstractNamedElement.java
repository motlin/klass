package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractNamedElement extends AbstractElement implements NamedElement
{
    @Nonnull
    private final ParserRuleContext nameContext;
    @Nonnull
    private final String            name;
    private final int               ordinal;

    protected AbstractNamedElement(
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

    @Override
    @Nonnull
    public final String getName()
    {
        return this.name;
    }

    @Override
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
