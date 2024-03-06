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
        extends AbstractOrdinalElement
        implements NamedElementWithSourceCode
{
    @Nonnull
    private final ParserRuleContext nameContext;

    protected AbstractNamedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull ParserRuleContext nameContext)
    {
        super(elementContext, macroElement, sourceCode, ordinal);
        this.nameContext = Objects.requireNonNull(nameContext);
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
    public String toString()
    {
        return this.getName();
    }

    public abstract static class NamedElementBuilder<BuiltElement extends AbstractNamedElement>
            extends OrdinalElementBuilder<BuiltElement>
    {
        @Nonnull
        protected final ParserRuleContext nameContext;

        protected NamedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull ParserRuleContext nameContext)
        {
            super(elementContext, macroElement, sourceCode, ordinal);
            this.nameContext = Objects.requireNonNull(nameContext);
        }

        @Nonnull
        public ParserRuleContext getNameContext()
        {
            return this.nameContext;
        }
    }
}
