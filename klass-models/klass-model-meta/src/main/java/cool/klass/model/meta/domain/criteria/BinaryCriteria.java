package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class BinaryCriteria extends Criteria
{
    @Nonnull
    protected final Criteria left;
    @Nonnull
    protected final Criteria right;

    protected BinaryCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Criteria left,
            @Nonnull Criteria right)
    {
        super(elementContext);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    @Nonnull
    public Criteria getLeft()
    {
        return this.left;
    }

    @Nonnull
    public Criteria getRight()
    {
        return this.right;
    }

    public abstract static class BinaryCriteriaBuilder extends CriteriaBuilder
    {
        @Nonnull
        protected final CriteriaBuilder left;
        @Nonnull
        protected final CriteriaBuilder right;

        protected BinaryCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull CriteriaBuilder left,
                @Nonnull CriteriaBuilder right)
        {
            super(elementContext);
            this.left = Objects.requireNonNull(left);
            this.right = Objects.requireNonNull(right);
        }

        @Nonnull
        @Override
        public abstract BinaryCriteria build();
    }
}
