package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public class StringOperator extends Operator
{
    protected StringOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    public static class StringOperatorBuilder extends OperatorBuilder
    {
        public StringOperatorBuilder(ParserRuleContext elementContext, String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Override
        public StringOperator build()
        {
            return new StringOperator(this.elementContext, this.operatorText);
        }
    }
}
