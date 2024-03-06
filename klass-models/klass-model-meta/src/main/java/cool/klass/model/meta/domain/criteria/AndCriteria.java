package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AndCriteria extends BinaryCriteria
{
    private AndCriteria(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull Criteria left,
            @Nonnull Criteria right)
    {
        super(elementContext, inferred, left, right);
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
                boolean inferred,
                @Nonnull CriteriaBuilder left,
                @Nonnull CriteriaBuilder right)
        {
            super(elementContext, inferred, left, right);
        }

        @Nonnull
        @Override
        public AndCriteria build()
        {
            return new AndCriteria(
                    this.elementContext,
                    this.inferred,
                    this.left.build(),
                    this.right.build());
        }
    }
}
