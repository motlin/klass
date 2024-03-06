package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractCriteria extends AbstractElement implements Criteria
{
    protected AbstractCriteria(@Nonnull ParserRuleContext elementContext, @Nonnull Optional<Element> macroElement)
    {
        super(elementContext, macroElement);
    }

    public abstract static class AbstractCriteriaBuilder<BuiltElement extends AbstractCriteria> extends ElementBuilder<BuiltElement>
    {
        protected AbstractCriteriaBuilder(@Nonnull ParserRuleContext elementContext, Optional<ElementBuilder<?>> macroElement)
        {
            super(elementContext, macroElement);
        }
    }
}
