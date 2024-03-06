package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Criteria extends Element
{
    protected Criteria(@Nonnull ParserRuleContext elementContext)
    {
        super(elementContext);
    }

    public abstract void visit(CriteriaVisitor visitor);

    public abstract static class CriteriaBuilder extends ElementBuilder
    {
        protected CriteriaBuilder(@Nonnull ParserRuleContext elementContext)
        {
            super(elementContext);
        }

        @Nonnull
        public abstract Criteria build();
    }
}
