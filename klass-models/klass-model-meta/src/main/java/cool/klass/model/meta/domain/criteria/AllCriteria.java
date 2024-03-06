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
    public void visit(@Nonnull CriteriaVisitor visitor)
    {
        visitor.visitAll(this);
    }
}
