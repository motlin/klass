package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.BinaryCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractBinaryCriteria extends AbstractCriteria implements BinaryCriteria
{
    @Nonnull
    protected final AbstractCriteria left;
    @Nonnull
    protected final AbstractCriteria right;

    protected AbstractBinaryCriteria(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull AbstractCriteria left,
            @Nonnull AbstractCriteria right)
    {
        super(elementContext, inferred);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    @Override
    @Nonnull
    public AbstractCriteria getLeft()
    {
        return this.left;
    }

    @Override
    @Nonnull
    public AbstractCriteria getRight()
    {
        return this.right;
    }

    public abstract static class AbstractBinaryCriteriaBuilder<BuiltElement extends AbstractBinaryCriteria>
            extends AbstractCriteriaBuilder<BuiltElement>
    {
        @Nonnull
        protected final AbstractCriteriaBuilder<?> left;
        @Nonnull
        protected final AbstractCriteriaBuilder<?> right;

        protected AbstractBinaryCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull AbstractCriteriaBuilder<?> left,
                @Nonnull AbstractCriteriaBuilder<?> right)
        {
            super(elementContext, inferred);
            this.left = Objects.requireNonNull(left);
            this.right = Objects.requireNonNull(right);
        }
    }
}
