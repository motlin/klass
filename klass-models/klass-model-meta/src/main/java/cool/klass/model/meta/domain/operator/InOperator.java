package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class InOperator extends Operator
{
    private InOperator(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitIn(this);
    }

    public static class InOperatorBuilder extends OperatorBuilder
    {
        public InOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Nonnull
        @Override
        public InOperator build()
        {
            return new InOperator(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
