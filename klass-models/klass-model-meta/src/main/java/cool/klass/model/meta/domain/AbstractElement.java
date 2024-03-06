package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractElement
        implements ElementWithSourceCode
{
    @Nonnull
    private final ParserRuleContext elementContext;
    @Nonnull
    private final Optional<Element> macroElement;
    /**
     * The type of sourceCode is null only for Elements that don't appear in source code, like PrimitiveType declarations
     */
    @Nullable
    private final SourceCode        sourceCode;

    protected AbstractElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.macroElement   = Objects.requireNonNull(macroElement);
        this.sourceCode     = sourceCode;
    }

    @Nonnull
    @Override
    public Optional<Element> getMacroElement()
    {
        return this.macroElement;
    }

    @Override
    public SourceCode getSourceCodeObject()
    {
        return Objects.requireNonNull(this.sourceCode);
    }

    @Override
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
        @Nullable
        protected final SourceCodeBuilder           sourceCode;
        protected       BuiltElement                element;

        protected ElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            this.elementContext = Objects.requireNonNull(elementContext);
            this.macroElement   = Objects.requireNonNull(macroElement);
            this.sourceCode     = sourceCode;
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
