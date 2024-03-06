package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.InequalityOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InequalityOperatorImpl extends AbstractOperator implements InequalityOperator
{
    private InequalityOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, operatorText);
    }

    public static final class InequalityOperatorBuilder extends AbstractOperatorBuilder<InequalityOperatorImpl>
    {
        public InequalityOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, operatorText);
        }

        @Override
        @Nonnull
        protected InequalityOperatorImpl buildUnsafe()
        {
            return new InequalityOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.operatorText);
        }
    }
}
