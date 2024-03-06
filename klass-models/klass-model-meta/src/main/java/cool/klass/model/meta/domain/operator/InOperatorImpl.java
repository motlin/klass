package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.InOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InOperatorImpl extends AbstractOperator implements InOperator
{
    private InOperatorImpl(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    public static class InOperatorBuilder extends OperatorBuilder
    {
        public InOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Nonnull
        @Override
        public InOperatorImpl build()
        {
            return new InOperatorImpl(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
