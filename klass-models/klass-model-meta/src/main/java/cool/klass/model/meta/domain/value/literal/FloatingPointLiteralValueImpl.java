package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.FloatingPointLiteralValue;
import cool.klass.model.meta.grammar.KlassParser.FloatingPointLiteralContext;

public final class FloatingPointLiteralValueImpl
        extends AbstractLiteralValue
        implements FloatingPointLiteralValue
{
    private final double value;

    private FloatingPointLiteralValueImpl(
            @Nonnull FloatingPointLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            double value)
    {
        super(elementContext, macroElement, sourceCode);
        this.value = value;
    }

    @Nonnull
    @Override
    public FloatingPointLiteralContext getElementContext()
    {
        return (FloatingPointLiteralContext) super.getElementContext();
    }

    @Override
    public double getValue()
    {
        return this.value;
    }

    public static final class FloatingPointLiteralValueBuilder
            extends AbstractLiteralValueBuilder<FloatingPointLiteralValueImpl>
    {
        private final double value;

        public FloatingPointLiteralValueBuilder(
                @Nonnull FloatingPointLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                double value)
        {
            super(elementContext, macroElement, sourceCode);
            this.value = value;
        }

        @Override
        @Nonnull
        protected FloatingPointLiteralValueImpl buildUnsafe()
        {
            return new FloatingPointLiteralValueImpl(
                    (FloatingPointLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.value);
        }
    }
}
