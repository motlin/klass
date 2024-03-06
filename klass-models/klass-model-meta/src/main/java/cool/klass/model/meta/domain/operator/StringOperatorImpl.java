package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.StringOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperatorImpl extends AbstractOperator implements StringOperator
{
    private StringOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    public static final class StringOperatorBuilder extends AbstractOperatorBuilder<StringOperatorImpl>
    {
        public StringOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Override
        @Nonnull
        protected StringOperatorImpl buildUnsafe()
        {
            return new StringOperatorImpl(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
