package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractCriteria
        extends AbstractElement
        implements Criteria
{
    protected AbstractCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    public abstract static class AbstractCriteriaBuilder<BuiltElement extends AbstractCriteria>
            extends ElementBuilder<BuiltElement>
    {
        protected AbstractCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }
    }
}
