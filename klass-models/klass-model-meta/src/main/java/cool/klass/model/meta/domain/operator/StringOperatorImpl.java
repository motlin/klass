package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.StringOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperatorImpl extends AbstractOperator implements StringOperator
{
    private StringOperatorImpl(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    public static class StringOperatorBuilder extends OperatorBuilder
    {
        public StringOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Nonnull
        @Override
        public StringOperatorImpl build()
        {
            return new StringOperatorImpl(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
