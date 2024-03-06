package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;

public final class UserLiteralImpl
        extends AbstractLiteralValue
        implements UserLiteral
{
    private UserLiteralImpl(
            @Nonnull NativeLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    @Nonnull
    @Override
    public NativeLiteralContext getElementContext()
    {
        return (NativeLiteralContext) super.getElementContext();
    }

    public static final class UserLiteralBuilder
            extends AbstractLiteralValueBuilder<UserLiteralImpl>
    {
        public UserLiteralBuilder(
                @Nonnull NativeLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }

        @Override
        @Nonnull
        protected UserLiteralImpl buildUnsafe()
        {
            return new UserLiteralImpl(
                    (NativeLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build());
        }
    }
}
