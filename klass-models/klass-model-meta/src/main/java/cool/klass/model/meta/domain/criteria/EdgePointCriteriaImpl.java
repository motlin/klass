package cool.klass.model.meta.domain.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.value.AbstractMemberReferencePath;
import cool.klass.model.meta.domain.value.AbstractMemberReferencePath.AbstractMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EdgePointCriteriaImpl extends AbstractCriteria implements EdgePointCriteria
{
    @Nonnull
    private final AbstractMemberReferencePath memberExpressionValue;

    private EdgePointCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull AbstractMemberReferencePath memberExpressionValue)
    {
        super(elementContext, macroElement);
        this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
    }

    @Override
    @Nonnull
    public AbstractMemberReferencePath getMemberExpressionValue()
    {
        return this.memberExpressionValue;
    }

    public static final class EdgePointCriteriaBuilder extends AbstractCriteriaBuilder<EdgePointCriteriaImpl>
    {
        @Nonnull
        private final AbstractMemberReferencePathBuilder<?> memberExpressionValue;

        public EdgePointCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull AbstractMemberReferencePathBuilder<?> memberExpressionValue)
        {
            super(elementContext, macroElement);
            this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
        }

        @Override
        @Nonnull
        protected EdgePointCriteriaImpl buildUnsafe()
        {
            return new EdgePointCriteriaImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.memberExpressionValue.build());
        }
    }
}
