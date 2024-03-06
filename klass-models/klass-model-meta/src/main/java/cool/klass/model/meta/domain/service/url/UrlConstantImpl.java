package cool.klass.model.meta.domain.service.url;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class UrlConstantImpl
        extends AbstractNamedElement
{
    private UrlConstantImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
    }

    public static final class UrlConstantBuilder
            extends NamedElementBuilder<UrlConstantImpl>
    {
        public UrlConstantBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        }

        @Override
        @Nonnull
        protected UrlConstantImpl buildUnsafe()
        {
            return new UrlConstantImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal);
        }
    }
}
