package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import org.antlr.v4.runtime.ParserRuleContext;

public final class UserLiteralImpl extends AbstractLiteralValue implements UserLiteral
{
    private UserLiteralImpl(@Nonnull ParserRuleContext elementContext, Optional<Element> macroElement)
    {
        super(elementContext, macroElement);
    }

    public static final class UserLiteralBuilder extends AbstractLiteralValueBuilder<UserLiteralImpl>
    {
        public UserLiteralBuilder(@Nonnull ParserRuleContext elementContext, Optional<ElementBuilder<?>> macroElement)
        {
            super(elementContext, macroElement);
        }

        @Override
        @Nonnull
        protected UserLiteralImpl buildUnsafe()
        {
            return new UserLiteralImpl(this.elementContext, this.macroElement.map(ElementBuilder::getElement));
        }
    }
}
