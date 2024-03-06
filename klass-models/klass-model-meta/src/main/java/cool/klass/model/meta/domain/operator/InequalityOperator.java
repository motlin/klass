package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public class InequalityOperator extends Operator
{
    protected InequalityOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    public static class InequalityOperatorBuilder extends OperatorBuilder
    {
        public InequalityOperatorBuilder(ParserRuleContext elementContext, String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Override
        public InequalityOperator build()
        {
            return new InequalityOperator(this.elementContext, this.operatorText);
        }
    }
}
