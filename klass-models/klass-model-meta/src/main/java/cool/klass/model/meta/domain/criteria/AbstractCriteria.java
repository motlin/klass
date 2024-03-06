package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractCriteria extends AbstractElement implements Criteria
{
    protected AbstractCriteria(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    public abstract static class CriteriaBuilder extends ElementBuilder
    {
        protected CriteriaBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Nonnull
        public abstract AbstractCriteria build();
    }
}
