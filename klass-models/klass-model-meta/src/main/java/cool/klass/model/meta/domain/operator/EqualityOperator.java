package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public class EqualityOperator extends Operator
{
    protected EqualityOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    public static class EqualityOperatorBuilder extends OperatorBuilder
    {
        public EqualityOperatorBuilder(ParserRuleContext elementContext, String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Override
        public EqualityOperator build()
        {
            return new EqualityOperator(this.elementContext, this.operatorText);
        }
    }
}
