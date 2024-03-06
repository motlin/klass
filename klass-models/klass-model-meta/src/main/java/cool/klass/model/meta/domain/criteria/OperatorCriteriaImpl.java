package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.operator.AbstractOperator;
import cool.klass.model.meta.domain.operator.AbstractOperator.AbstractOperatorBuilder;
import cool.klass.model.meta.domain.value.AbstractExpressionValue;
import cool.klass.model.meta.domain.value.AbstractExpressionValue.AbstractExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OperatorCriteriaImpl extends AbstractCriteria implements OperatorCriteria
{
    @Nonnull
    private final AbstractOperator        operator;
    @Nonnull
    private final AbstractExpressionValue sourceValue;
    @Nonnull
    private final AbstractExpressionValue targetValue;

    private OperatorCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull AbstractOperator operator,
            @Nonnull AbstractExpressionValue sourceValue,
            @Nonnull AbstractExpressionValue targetValue)
    {
        super(elementContext, inferred);
        this.operator = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
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

    public static final class OperatorCriteriaBuilder extends AbstractCriteriaBuilder<OperatorCriteriaImpl>
    {
        @Nonnull
        private final AbstractOperatorBuilder<?>        operator;
        @Nonnull
        private final AbstractExpressionValueBuilder<?> sourceValue;
        @Nonnull
        private final AbstractExpressionValueBuilder<?> targetValue;

        public OperatorCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull AbstractOperatorBuilder<?> operator,
                @Nonnull AbstractExpressionValueBuilder<?> sourceValue,
                @Nonnull AbstractExpressionValueBuilder<?> targetValue)
        {
            super(elementContext, inferred);
            this.operator = Objects.requireNonNull(operator);
            this.sourceValue = Objects.requireNonNull(sourceValue);
            this.targetValue = Objects.requireNonNull(targetValue);
        }

        @Override
        @Nonnull
        protected OperatorCriteriaImpl buildUnsafe()
        {
            return new OperatorCriteriaImpl(
                    this.elementContext,
                    this.inferred,
                    this.operator.build(),
                    this.sourceValue.build(),
                    this.targetValue.build());
        }
    }
}
