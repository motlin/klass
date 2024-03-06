package cool.klass.model.meta.domain.criteria;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class AllCriteria extends Criteria
{
    // TODO: Make a distinction between inferred and declaration
    public static final AllCriteria INSTANCE = new AllCriteria(new ParserRuleContext(), false);

    private AllCriteria(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
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
        public AllCriteriaBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Nonnull
        @Override
        public AllCriteria build()
        {
            return INSTANCE;
        }
    }
}
