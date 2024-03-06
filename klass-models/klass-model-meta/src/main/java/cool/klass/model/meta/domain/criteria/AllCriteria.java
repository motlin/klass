package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AllCriteria extends Criteria
{
    public static final AllCriteria INSTANCE = new AllCriteria(new ParserRuleContext());

    private AllCriteria(@Nonnull ParserRuleContext elementContext)
    {
        super(elementContext);
    }

    @Override
    public String getSourceCode()
    {
        return "all";
    }

    @Override
    public void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitAll(this);
    }

    public static final class AllCriteriaBuilder extends CriteriaBuilder
    {
        public AllCriteriaBuilder(@Nonnull ParserRuleContext elementContext)
        {
            super(elementContext);
        }

        @Nonnull
        @Override
        public AllCriteria build()
        {
            return INSTANCE;
        }
    }
}
