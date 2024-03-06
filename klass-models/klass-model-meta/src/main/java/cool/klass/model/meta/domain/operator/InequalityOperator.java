package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public final class InequalityOperator extends Operator
{
    private InequalityOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitInequality(this);
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
