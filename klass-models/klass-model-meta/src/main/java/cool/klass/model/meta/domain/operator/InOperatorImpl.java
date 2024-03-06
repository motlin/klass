package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.InOperator;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InOperatorImpl
        extends AbstractOperator
        implements InOperator
{
    private InOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, sourceCode, operatorText);
    }

    public static final class InOperatorBuilder
            extends AbstractOperatorBuilder<InOperatorImpl>
    {
        public InOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, sourceCode, operatorText);
        }

        @Override
        @Nonnull
        protected InOperatorImpl buildUnsafe()
        {
            return new InOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.operatorText);
        }
    }
}
