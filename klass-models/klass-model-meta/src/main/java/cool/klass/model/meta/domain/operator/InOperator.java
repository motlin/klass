package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public final class InOperator extends Operator
{
    private InOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitIn(this);
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
