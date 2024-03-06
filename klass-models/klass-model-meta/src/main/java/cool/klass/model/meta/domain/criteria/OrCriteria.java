package cool.klass.model.meta.domain.criteria;

import org.antlr.v4.runtime.ParserRuleContext;

public final class OrCriteria extends BinaryCriteria
{
    private OrCriteria(
            ParserRuleContext elementContext,
            Criteria left,
            Criteria right)
    {
        super(elementContext, left, right);
    }

    @Override
    public void visit(CriteriaVisitor visitor)
    {
        visitor.visitOr(this);
    }

    public static final class OrCriteriaBuilder extends BinaryCriteriaBuilder
    {
        public OrCriteriaBuilder(
                ParserRuleContext elementContext,
                CriteriaBuilder left,
                CriteriaBuilder right)
        {
            super(elementContext, left, right);
        }

        @Override
        public OrCriteria build()
        {
            return new OrCriteria(
                    this.elementContext,
                    this.left.build(),
                    this.right.build());
        }
    }
}
