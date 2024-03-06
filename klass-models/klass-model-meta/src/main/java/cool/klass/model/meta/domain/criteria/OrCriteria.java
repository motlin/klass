package cool.klass.model.meta.domain.criteria;

import org.antlr.v4.runtime.ParserRuleContext;

public class OrCriteria extends BinaryCriteria
{
    protected OrCriteria(
            ParserRuleContext elementContext,
            Criteria left,
            Criteria right)
    {
        super(elementContext, left, right);
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
