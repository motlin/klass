package cool.klass.model.meta.domain;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractIdentifierElement
        extends AbstractNamedElement
{
    protected AbstractIdentifierElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull IdentifierContext nameContext,
            int ordinal)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal);
    }

    @Nonnull
    @Override
    public IdentifierContext getNameContext()
    {
        return (IdentifierContext) super.getNameContext();
    }

    public abstract static class IdentifierElementBuilder<BuiltElement extends AbstractIdentifierElement>
            extends NamedElementBuilder<BuiltElement>
    {
        protected IdentifierElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull IdentifierContext nameContext,
                int ordinal)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal);
        }

        @Nonnull
        @Override
        public IdentifierContext getNameContext()
        {
            return (IdentifierContext) super.getNameContext();
        }
    }
}
