package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.StringOperator;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperatorImpl
        extends AbstractOperator
        implements StringOperator
{
    private StringOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, sourceCode, operatorText);
    }

    public static final class StringOperatorBuilder
            extends AbstractOperatorBuilder<StringOperatorImpl>
    {
        public StringOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, sourceCode, operatorText);
        }

        @Override
        @Nonnull
        protected StringOperatorImpl buildUnsafe()
        {
            return new StringOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.operatorText);
        }
    }
}
