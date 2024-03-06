package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import org.antlr.v4.runtime.ParserRuleContext;

public final class IntegerLiteralValueImpl
        extends AbstractLiteralValue
        implements IntegerLiteralValue
{
    private final int value;

    private IntegerLiteralValueImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            int value)
    {
        super(elementContext, macroElement, sourceCode);
        this.value = value;
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
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
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
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.value);
        }
    }
}
