package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.InOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InOperatorImpl extends AbstractOperator implements InOperator
{
    private InOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, operatorText);
    }

    public static final class InOperatorBuilder extends AbstractOperatorBuilder<InOperatorImpl>
    {
        public InOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, operatorText);
        }

        @Override
        @Nonnull
        protected InOperatorImpl buildUnsafe()
        {
            return new InOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.operatorText);
        }
    }
}
