package cool.klass.model.meta.domain.service.url;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractIdentifierElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.UrlConstantContext;

public final class UrlConstantImpl
        extends AbstractIdentifierElement
{
    private UrlConstantImpl(
            @Nonnull UrlConstantContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
    }

    @Nonnull
    @Override
    public UrlConstantContext getElementContext()
    {
        return (UrlConstantContext) super.getElementContext();
    }

    public static final class UrlConstantBuilder
            extends IdentifierElementBuilder<UrlConstantImpl>
    {
        public UrlConstantBuilder(
                @Nonnull UrlConstantContext elementContext,
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
                    (UrlConstantContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext());
        }
    }
}
