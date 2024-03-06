package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AndCriteriaImpl extends AbstractBinaryCriteria implements AndCriteria
{
    private AndCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull AbstractCriteria left,
            @Nonnull AbstractCriteria right)
    {
        super(elementContext, macroElement, left, right);
    }

    public static final class AndCriteriaBuilder extends AbstractBinaryCriteriaBuilder<AndCriteriaImpl>
    {
        public AndCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull AbstractCriteriaBuilder<?> left,
                @Nonnull AbstractCriteriaBuilder<?> right)
        {
            super(elementContext, macroElement, left, right);
        }

        @Override
        @Nonnull
        protected AndCriteriaImpl buildUnsafe()
        {
            return new AndCriteriaImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.left.build(),
                    this.right.build());
        }
    }
}
