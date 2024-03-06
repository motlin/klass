package cool.klass.model.meta.domain.operator;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperator extends Operator
{
    private StringOperator(@Nonnull ParserRuleContext elementContext, @Nonnull String operatorText)
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
        public StringOperatorBuilder(@Nonnull ParserRuleContext elementContext, @Nonnull String operatorText)
        {
            super(elementContext, operatorText);
        }

        @Nonnull
        @Override
        public StringOperator build()
        {
            return new StringOperator(this.elementContext, this.operatorText);
        }
    }
}
