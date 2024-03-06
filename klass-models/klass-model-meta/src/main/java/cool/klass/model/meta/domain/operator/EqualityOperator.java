package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public final class EqualityOperator extends Operator
{
    private EqualityOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitEquality(this);
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
