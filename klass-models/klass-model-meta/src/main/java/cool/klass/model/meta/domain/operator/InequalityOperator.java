package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class InequalityOperator extends Operator
{
    private InequalityOperator(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitInequality(this);
    }

    public static class InequalityOperatorBuilder extends OperatorBuilder
    {
        public InequalityOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Nonnull
        @Override
        public InequalityOperator build()
        {
            return new InequalityOperator(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
