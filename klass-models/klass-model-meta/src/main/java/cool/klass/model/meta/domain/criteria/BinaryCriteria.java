package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class BinaryCriteria extends Criteria
{
    protected final Criteria left;
    protected final Criteria right;

    protected BinaryCriteria(
            ParserRuleContext elementContext,
            Criteria left,
            Criteria right)
    {
        super(elementContext);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    public Criteria getLeft()
    {
        return this.left;
    }

    public Criteria getRight()
    {
        return this.right;
    }

    @Override
    public void visit(CriteriaVisitor visitor)
    {
        visitor.visitBinary(this);
    }

    public abstract static class BinaryCriteriaBuilder extends CriteriaBuilder
    {
        protected final CriteriaBuilder left;
        protected final CriteriaBuilder right;

        protected BinaryCriteriaBuilder(
                ParserRuleContext elementContext,
                CriteriaBuilder left,
                CriteriaBuilder right)
        {
            super(elementContext);
            this.left = Objects.requireNonNull(left);
            this.right = Objects.requireNonNull(right);
        }

        @Override
        public abstract BinaryCriteria build();
    }
}
