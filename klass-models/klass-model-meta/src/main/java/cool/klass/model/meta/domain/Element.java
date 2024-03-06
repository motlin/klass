package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Element
{
    public static final ParserRuleContext NO_CONTEXT = new ParserRuleContext();

    @Nonnull
    private final ParserRuleContext elementContext;

    protected Element(@Nonnull ParserRuleContext elementContext)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
    }

    @Nonnull
    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    public abstract static class ElementBuilder
    {
        @Nonnull
        protected final ParserRuleContext elementContext;

        protected ElementBuilder(@Nonnull ParserRuleContext elementContext)
        {
            this.elementContext = Objects.requireNonNull(elementContext);
        }

        @Nonnull
        public ParserRuleContext getElementContext()
        {
            return this.elementContext;
        }
    }
}
