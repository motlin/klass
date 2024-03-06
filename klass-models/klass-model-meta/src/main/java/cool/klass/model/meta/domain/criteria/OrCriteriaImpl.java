package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrCriteriaImpl extends AbstractBinaryCriteria implements OrCriteria
{
    private OrCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull AbstractCriteria left,
            @Nonnull AbstractCriteria right)
    {
        super(elementContext, macroElement, left, right);
    }

    public static final class OrCriteriaBuilder extends AbstractBinaryCriteriaBuilder<OrCriteriaImpl>
    {
        public OrCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull AbstractCriteriaBuilder<?> left,
                @Nonnull AbstractCriteriaBuilder<?> right)
        {
            super(elementContext, macroElement, left, right);
        }

        @Override
        @Nonnull
        protected OrCriteriaImpl buildUnsafe()
        {
            return new OrCriteriaImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.left.build(),
                    this.right.build());
        }
    }
}
