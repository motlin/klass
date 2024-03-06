package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

public abstract class AbstractElement
        implements Element
{
    public static final ParserRuleContext NO_CONTEXT = new ParserRuleContext();

    @Nonnull
    private final ParserRuleContext    elementContext;
    @Nonnull
    private final Optional<Element>    macroElement;
    @Nonnull
    private final Optional<SourceCode> sourceCode;

    protected AbstractElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.macroElement   = Objects.requireNonNull(macroElement);
        this.sourceCode     = Objects.requireNonNull(sourceCode);
    }

    @Nonnull
    @Override
    public Optional<Element> getMacroElement()
    {
        return this.macroElement;
    }

    @Override
    public Optional<SourceCode> getSourceCodeObject()
    {
        return this.sourceCode;
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

    public abstract static class ElementBuilder<BuiltElement extends Element>
    {
        @Nonnull
        protected final ParserRuleContext           elementContext;
        @Nonnull
        protected final Optional<ElementBuilder<?>> macroElement;
        @Nonnull
        protected final Optional<SourceCodeBuilder> sourceCode;
        protected       BuiltElement                element;

        protected ElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode)
        {
            this.elementContext = Objects.requireNonNull(elementContext);
            this.macroElement   = Objects.requireNonNull(macroElement);
            this.sourceCode     = Objects.requireNonNull(sourceCode);
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
