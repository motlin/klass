package cool.klass.model.meta.domain.operator;

import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperator extends Operator
{
    private StringOperator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitString(this);
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
