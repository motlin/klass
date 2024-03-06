package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrCriteriaImpl extends AbstractBinaryCriteria implements OrCriteria
{
    private OrCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull AbstractCriteria left,
            @Nonnull AbstractCriteria right)
    {
        super(elementContext, inferred, left, right);
    }

    public static final class OrCriteriaBuilder extends BinaryCriteriaBuilder
    {
        public OrCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull CriteriaBuilder left,
                @Nonnull CriteriaBuilder right)
        {
            super(elementContext, inferred, left, right);
        }

        @Nonnull
        @Override
        public OrCriteriaImpl build()
        {
            return new OrCriteriaImpl(
                    this.elementContext,
                    this.inferred,
                    this.left.build(),
                    this.right.build());
        }
    }
}
