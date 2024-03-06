package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.EqualityOperator;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EqualityOperatorImpl extends AbstractOperator implements EqualityOperator
{
    private EqualityOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, sourceCode, operatorText);
    }

    public static final class EqualityOperatorBuilder extends AbstractOperatorBuilder<EqualityOperatorImpl>
    {
        public EqualityOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, sourceCode, operatorText);
        }

        @Override
        @Nonnull
        protected EqualityOperatorImpl buildUnsafe()
        {
            return new EqualityOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.operatorText);
        }
    }
}
