package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import cool.klass.model.meta.grammar.KlassParser.NullLiteralContext;

public final class NullLiteralImpl
        extends AbstractLiteralValue
        implements NullLiteral
{
    private NullLiteralImpl(
            @Nonnull NullLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    @Nonnull
    @Override
    public NullLiteralContext getElementContext()
    {
        return (NullLiteralContext) super.getElementContext();
    }

    public static final class NullLiteralBuilder
            extends AbstractLiteralValueBuilder<NullLiteralImpl>
    {
        public NullLiteralBuilder(
                @Nonnull NullLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }

        @Override
        @Nonnull
        protected NullLiteralImpl buildUnsafe()
        {
            return new NullLiteralImpl(
                    (NullLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build());
        }
    }
}
