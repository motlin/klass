package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.InequalityOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InequalityOperatorImpl extends AbstractOperator implements InequalityOperator
{
    private InequalityOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    public static final class InequalityOperatorBuilder extends AbstractOperatorBuilder<InequalityOperatorImpl>
    {
        public InequalityOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Override
        @Nonnull
        protected InequalityOperatorImpl buildUnsafe()
        {
            return new InequalityOperatorImpl(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
