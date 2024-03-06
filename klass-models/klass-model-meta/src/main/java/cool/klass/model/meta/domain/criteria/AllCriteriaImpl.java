package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AllCriteriaImpl extends AbstractCriteria implements AllCriteria
{
    // TODO: Make a distinction between inferred and declaration
    public static final AllCriteriaImpl INSTANCE = new AllCriteriaImpl(new ParserRuleContext(), false);

    private AllCriteriaImpl(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    @Nonnull
    @Override
    public String getSourceCode()
    {
        return "all";
    }

    public static final class AllCriteriaBuilder extends CriteriaBuilder
    {
        public AllCriteriaBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Nonnull
        @Override
        public AllCriteriaImpl build()
        {
            return INSTANCE;
        }
    }
}
