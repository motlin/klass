package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AndCriteria extends BinaryCriteria
{
    private AndCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Criteria left,
            @Nonnull Criteria right)
    {
        super(elementContext, left, right);
    }

    @Override
    public void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitAnd(this);
    }

    public static final class AndCriteriaBuilder extends BinaryCriteriaBuilder
    {
        public AndCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull CriteriaBuilder left,
                @Nonnull CriteriaBuilder right)
        {
            super(elementContext, left, right);
        }

        @Nonnull
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
