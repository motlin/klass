package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public class InOperator extends Operator
{
    protected InOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    public static class InOperatorBuilder extends OperatorBuilder
    {
        public InOperatorBuilder(ParserRuleContext elementContext, String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Override
        public InOperator build()
        {
            return new InOperator(this.elementContext, this.operatorText);
        }
    }
}
