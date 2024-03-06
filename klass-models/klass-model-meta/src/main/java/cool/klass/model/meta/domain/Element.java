package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

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

    public String getSourceCode()
    {
        Interval interval = new Interval(
                this.elementContext.getStart().getStartIndex(),
                this.elementContext.getStop().getStopIndex());
        return this.elementContext.getStart().getInputStream().getText(interval);
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
