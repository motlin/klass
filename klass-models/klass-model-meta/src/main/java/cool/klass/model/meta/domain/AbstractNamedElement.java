package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.NamedElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public abstract class AbstractNamedElement
        extends AbstractElement
        implements NamedElementWithSourceCode
{
    @Nonnull
    private final ParserRuleContext nameContext;
    private final int               ordinal;

    protected AbstractNamedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            int ordinal)
    {
        super(elementContext, macroElement, sourceCode);
        this.nameContext = Objects.requireNonNull(nameContext);
        this.ordinal     = ordinal;
    }

    @Nonnull
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Override
    @Nonnull
    public Token getNameToken()
    {
        return this.nameContext.getStart();
    }

    @Override
    @Nonnull
    public final String getName()
    {
        return this.nameContext.getText();
    }

    @Override
    public int getOrdinal()
    {
        return this.ordinal;
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    public abstract static class NamedElementBuilder<BuiltElement extends AbstractNamedElement>
            extends ElementBuilder<BuiltElement>
    {
        @Nonnull
        protected final ParserRuleContext nameContext;
        protected final int               ordinal;

        protected NamedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                int ordinal)
        {
            super(elementContext, macroElement, sourceCode);
            this.nameContext = Objects.requireNonNull(nameContext);
            this.ordinal     = ordinal;
        }

        @Nonnull
        public ParserRuleContext getNameContext()
        {
            return this.nameContext;
        }
    }
}
