package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperator extends Operator
{
    private StringOperator(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred, operatorText);
    }

    @Override
    public void visit(OperatorVisitor visitor)
    {
        visitor.visitString(this);
    }

    public static class StringOperatorBuilder extends OperatorBuilder
    {
        public StringOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred, operatorText);
        }

        @Nonnull
        @Override
        public StringOperator build()
        {
            return new StringOperator(this.elementContext, this.inferred, this.operatorText);
        }
    }
}
