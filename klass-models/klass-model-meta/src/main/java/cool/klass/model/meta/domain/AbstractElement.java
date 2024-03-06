package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

public abstract class AbstractElement implements Element
{
    public static final ParserRuleContext NO_CONTEXT = new ParserRuleContext();

    @Nonnull
    private final ParserRuleContext elementContext;
    private final boolean           inferred;

    protected AbstractElement(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.inferred = inferred;
    }

    @Override
    public boolean isInferred()
    {
        return this.inferred;
    }

    @Nonnull
    @Override
    public String getSourceCode()
    {
        Interval interval = new Interval(
                this.elementContext.getStart().getStartIndex(),
                this.elementContext.getStop().getStopIndex());
        return this.elementContext.getStart().getInputStream().getText(interval);
    }

    @Nonnull
    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    public interface IElementBuilder
    {
    }

    public abstract static class ElementBuilder<BuiltElement extends AbstractElement>
            implements IElementBuilder
    {
        @Nonnull
        protected final ParserRuleContext elementContext;
        protected final boolean           inferred;
        protected       BuiltElement      element;

        protected ElementBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            this.elementContext = Objects.requireNonNull(elementContext);
            this.inferred = inferred;
        }

        @Nonnull
        public final BuiltElement build()
        {
            if (this.element != null)
            {
                throw new IllegalStateException();
            }
            this.element = Objects.requireNonNull(this.buildUnsafe());
            this.buildChildren();
            return this.element;
        }

        @Nonnull
        protected abstract BuiltElement buildUnsafe();

        protected void buildChildren()
        {
        }

        @Nonnull
        public final BuiltElement getElement()
        {
            return Objects.requireNonNull(this.element);
        }
    }
}
