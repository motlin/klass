package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AllCriteriaImpl extends AbstractCriteria implements AllCriteria
{
    // TODO: Make a distinction between macroElement and declaration
    public static final AllCriteriaImpl INSTANCE = new AllCriteriaImpl(new ParserRuleContext(), Optional.empty());

    private AllCriteriaImpl(@Nonnull ParserRuleContext elementContext, Optional<Element> macroElement)
    {
        super(elementContext, macroElement);
    }

    @Nonnull
    @Override
    public String getSourceCode()
    {
        return "all";
    }

    public static final class AllCriteriaBuilder extends AbstractCriteriaBuilder<AllCriteriaImpl>
    {
        public AllCriteriaBuilder(@Nonnull ParserRuleContext elementContext, Optional<ElementBuilder<?>> macroElement)
        {
            super(elementContext, macroElement);
        }

        @Nonnull
        @Override
        protected AllCriteriaImpl buildUnsafe()
        {
            return INSTANCE;
        }
    }
}
