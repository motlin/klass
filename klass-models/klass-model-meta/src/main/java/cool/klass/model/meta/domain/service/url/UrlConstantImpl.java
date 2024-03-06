package cool.klass.model.meta.domain.service.url;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractIdentifierElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public final class UrlConstantImpl
        extends AbstractIdentifierElement
{
    private UrlConstantImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
    }

    public static final class UrlConstantBuilder
            extends IdentifierElementBuilder<UrlConstantImpl>
    {
        public UrlConstantBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
        }

        @Override
        @Nonnull
        protected UrlConstantImpl buildUnsafe()
        {
            return new UrlConstantImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext());
        }
    }
}
