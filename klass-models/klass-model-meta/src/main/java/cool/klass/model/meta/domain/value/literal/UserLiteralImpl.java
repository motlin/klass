package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;

public final class UserLiteralImpl
        extends AbstractLiteralValue
        implements UserLiteral
{
    @Nonnull
    private final KlassWithSourceCode userClass;

    private UserLiteralImpl(
            @Nonnull NativeLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull KlassWithSourceCode userClass)
    {
        super(elementContext, macroElement, sourceCode);
        this.userClass = Objects.requireNonNull(userClass);
    }

    @Nonnull
    @Override
    public NativeLiteralContext getElementContext()
    {
        return (NativeLiteralContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public KlassWithSourceCode getUserClass()
    {
        return this.userClass;
    }

    public static final class UserLiteralBuilder
            extends AbstractLiteralValueBuilder<UserLiteralImpl>
    {
        private final KlassBuilder userClassBuilder;

        public UserLiteralBuilder(
                @Nonnull NativeLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull KlassBuilder userClassBuilder)
        {
            super(elementContext, macroElement, sourceCode);
            this.userClassBuilder = Objects.requireNonNull(userClassBuilder);
        }

        @Override
        @Nonnull
        protected UserLiteralImpl buildUnsafe()
        {
            return new UserLiteralImpl(
                    (NativeLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.userClassBuilder.getElement());
        }
    }
}
