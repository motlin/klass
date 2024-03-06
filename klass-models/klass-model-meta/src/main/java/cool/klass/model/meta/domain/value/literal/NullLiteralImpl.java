package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import org.antlr.v4.runtime.ParserRuleContext;

public final class NullLiteralImpl extends AbstractLiteralValue implements NullLiteral
{
    private NullLiteralImpl(@Nonnull ParserRuleContext elementContext, Optional<Element> macroElement)
    {
        super(elementContext, macroElement);
    }

    public static final class NullLiteralBuilder extends AbstractLiteralValueBuilder<NullLiteralImpl>
    {
        public NullLiteralBuilder(@Nonnull ParserRuleContext elementContext, Optional<ElementBuilder<?>> macroElement)
        {
            super(elementContext, macroElement);
        }

        @Override
        @Nonnull
        protected NullLiteralImpl buildUnsafe()
        {
            return new NullLiteralImpl(this.elementContext, this.macroElement.map(ElementBuilder::getElement));
        }
    }
}
