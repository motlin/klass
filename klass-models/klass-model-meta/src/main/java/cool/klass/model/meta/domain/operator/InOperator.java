package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class InOperator extends Operator
{
    private InOperator(@Nonnull ParserRuleContext elementContext, @Nonnull String operatorText)
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
        public InOperatorBuilder(@Nonnull ParserRuleContext elementContext, @Nonnull String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Nonnull
        @Override
        public InOperator build()
        {
            return new InOperator(this.elementContext, this.operatorText);
        }
    }
}
