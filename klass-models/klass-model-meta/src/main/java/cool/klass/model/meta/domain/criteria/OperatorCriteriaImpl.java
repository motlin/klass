package cool.klass.model.meta.domain.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.operator.AbstractOperator;
import cool.klass.model.meta.domain.operator.AbstractOperator.AbstractOperatorBuilder;
import cool.klass.model.meta.domain.value.AbstractExpressionValue;
import cool.klass.model.meta.domain.value.AbstractExpressionValue.AbstractExpressionValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;

public final class OperatorCriteriaImpl
        extends AbstractCriteria
        implements OperatorCriteria
{
    @Nonnull
    private final AbstractOperator        operator;
    @Nonnull
    private final AbstractExpressionValue sourceValue;
    @Nonnull
    private final AbstractExpressionValue targetValue;

    private OperatorCriteriaImpl(
            @Nonnull CriteriaOperatorContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractOperator operator,
            @Nonnull AbstractExpressionValue sourceValue,
            @Nonnull AbstractExpressionValue targetValue)
    {
        super(elementContext, macroElement, sourceCode);
        this.operator    = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    @Nonnull
    @Override
    public CriteriaOperatorContext getElementContext()
    {
        return (CriteriaOperatorContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public AbstractOperator getOperator()
    {
        return this.operator;
    }

    @Override
    @Nonnull
    public AbstractExpressionValue getSourceValue()
    {
        return this.sourceValue;
    }

    @Override
    @Nonnull
    public AbstractExpressionValue getTargetValue()
    {
        return this.targetValue;
    }

    public static final class OperatorCriteriaBuilder
            extends AbstractCriteriaBuilder<OperatorCriteriaImpl>
    {
        @Nonnull
        private final AbstractOperatorBuilder<?>        operator;
        @Nonnull
        private final AbstractExpressionValueBuilder<?> sourceValue;
        @Nonnull
        private final AbstractExpressionValueBuilder<?> targetValue;

        public OperatorCriteriaBuilder(
                @Nonnull CriteriaOperatorContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull AbstractOperatorBuilder<?> operator,
                @Nonnull AbstractExpressionValueBuilder<?> sourceValue,
                @Nonnull AbstractExpressionValueBuilder<?> targetValue)
        {
            super(elementContext, macroElement, sourceCode);
            this.operator    = Objects.requireNonNull(operator);
            this.sourceValue = Objects.requireNonNull(sourceValue);
            this.targetValue = Objects.requireNonNull(targetValue);
        }

        @Override
        @Nonnull
        protected OperatorCriteriaImpl buildUnsafe()
        {
            return new OperatorCriteriaImpl(
                    (CriteriaOperatorContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.operator.build(),
                    this.sourceValue.build(),
                    this.targetValue.build());
        }
    }
}
