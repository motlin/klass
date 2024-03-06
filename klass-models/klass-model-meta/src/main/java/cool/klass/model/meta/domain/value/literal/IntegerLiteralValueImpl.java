package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;

public final class IntegerLiteralValueImpl
        extends AbstractLiteralValue
        implements IntegerLiteralValue
{
    private final int value;

    private IntegerLiteralValueImpl(
            @Nonnull IntegerLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int value)
    {
        super(elementContext, macroElement, sourceCode);
        this.value = value;
    }

    @Nonnull
    @Override
    public IntegerLiteralContext getElementContext()
    {
        return (IntegerLiteralContext) super.getElementContext();
    }

    @Override
    public int getValue()
    {
        return this.value;
    }

    public static final class IntegerLiteralValueBuilder
            extends AbstractLiteralValueBuilder<IntegerLiteralValueImpl>
    {
        private final int value;

        public IntegerLiteralValueBuilder(
                @Nonnull IntegerLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int value)
        {
            super(elementContext, macroElement, sourceCode);
            this.value = value;
        }

        @Override
        @Nonnull
        protected IntegerLiteralValueImpl buildUnsafe()
        {
            return new IntegerLiteralValueImpl(
                    (IntegerLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.value);
        }
    }
}
