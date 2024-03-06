package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class EqualityOperator extends Operator
{
    private EqualityOperator(@Nonnull ParserRuleContext elementContext, @Nonnull String operatorText)
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
        public EqualityOperatorBuilder(@Nonnull ParserRuleContext elementContext, @Nonnull String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Nonnull
        @Override
        public EqualityOperator build()
        {
            return new EqualityOperator(this.elementContext, this.operatorText);
        }
    }
}
