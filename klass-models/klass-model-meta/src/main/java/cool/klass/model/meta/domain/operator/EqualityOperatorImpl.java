package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.EqualityOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EqualityOperatorImpl extends AbstractOperator implements EqualityOperator
{
    private EqualityOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, operatorText);
    }

    public static final class EqualityOperatorBuilder extends AbstractOperatorBuilder<EqualityOperatorImpl>
    {
        public EqualityOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, operatorText);
        }

        @Override
        @Nonnull
        protected EqualityOperatorImpl buildUnsafe()
        {
            return new EqualityOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.operatorText);
        }
    }
}
