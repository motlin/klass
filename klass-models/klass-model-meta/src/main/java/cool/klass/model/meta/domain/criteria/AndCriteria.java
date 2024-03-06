package cool.klass.model.meta.domain.criteria;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AndCriteria extends BinaryCriteria
{
    private AndCriteria(
            ParserRuleContext elementContext,
            Criteria left,
            Criteria right)
    {
        super(elementContext, left, right);
    }

    @Override
    public void visit(CriteriaVisitor visitor)
    {
        visitor.visitAnd(this);
    }

    public static final class AndCriteriaBuilder extends BinaryCriteriaBuilder
    {
        public AndCriteriaBuilder(
                ParserRuleContext elementContext,
                CriteriaBuilder left,
                CriteriaBuilder right)
        {
            super(elementContext, left, right);
        }

        @Override
        public AndCriteria build()
        {
            return new AndCriteria(
                    this.elementContext,
                    this.left.build(),
                    this.right.build());
        }
    }
}
