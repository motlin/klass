package cool.klass.model.meta.domain.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.value.AbstractMemberReferencePath;
import cool.klass.model.meta.domain.value.AbstractMemberReferencePath.AbstractMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;

public final class EdgePointCriteriaImpl
        extends AbstractCriteria
        implements EdgePointCriteria
{
    @Nonnull
    private final AbstractMemberReferencePath memberExpressionValue;

    private EdgePointCriteriaImpl(
            @Nonnull CriteriaEdgePointContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractMemberReferencePath memberExpressionValue)
    {
        super(elementContext, macroElement, sourceCode);
        this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
    }

    @Nonnull
    @Override
    public CriteriaEdgePointContext getElementContext()
    {
        return (CriteriaEdgePointContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public AbstractMemberReferencePath getMemberExpressionValue()
    {
        return this.memberExpressionValue;
    }

    public static final class EdgePointCriteriaBuilder
            extends AbstractCriteriaBuilder<EdgePointCriteriaImpl>
    {
        @Nonnull
        private final AbstractMemberReferencePathBuilder<?> memberExpressionValue;

        public EdgePointCriteriaBuilder(
                @Nonnull CriteriaEdgePointContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull AbstractMemberReferencePathBuilder<?> memberExpressionValue)
        {
            super(elementContext, macroElement, sourceCode);
            this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
        }

        @Override
        @Nonnull
        protected EdgePointCriteriaImpl buildUnsafe()
        {
            return new EdgePointCriteriaImpl(
                    (CriteriaEdgePointContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.memberExpressionValue.build());
        }
    }
}
