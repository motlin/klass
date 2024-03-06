package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.EqualityOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EqualityOperatorImpl extends AbstractOperator implements EqualityOperator
{
    private EqualityOperatorImpl(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    public static class EqualityOperatorBuilder extends OperatorBuilder
    {
        public EqualityOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Nonnull
        @Override
        public EqualityOperatorImpl build()
        {
            return new EqualityOperatorImpl(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
