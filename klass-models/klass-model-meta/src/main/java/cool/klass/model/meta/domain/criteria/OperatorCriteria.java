package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.operator.Operator;
import cool.klass.model.meta.domain.operator.Operator.OperatorBuilder;
import cool.klass.model.meta.domain.value.ExpressionValue;
import cool.klass.model.meta.domain.value.ExpressionValue.ExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OperatorCriteria extends Criteria
{
    @Nonnull
    private final Operator        operator;
    @Nonnull
    private final ExpressionValue sourceValue;
    @Nonnull
    private final ExpressionValue targetValue;

    private OperatorCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Operator operator,
            @Nonnull ExpressionValue sourceValue,
            @Nonnull ExpressionValue targetValue)
    {
        super(elementContext);
        this.operator = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    @Nonnull
    public Operator getOperator()
    {
        return this.operator;
    }

    @Nonnull
    public ExpressionValue getSourceValue()
    {
        return this.sourceValue;
    }

    @Nonnull
    public ExpressionValue getTargetValue()
    {
        return this.targetValue;
    }

    @Override
    public void visit(CriteriaVisitor visitor)
    {
        visitor.visitOperator(this);
    }

    public static final class OperatorCriteriaBuilder extends CriteriaBuilder
    {
        @Nonnull
        private final OperatorBuilder        operator;
        @Nonnull
        private final ExpressionValueBuilder sourceValue;
        @Nonnull
        private final ExpressionValueBuilder targetValue;

        public OperatorCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull OperatorBuilder operator,
                @Nonnull ExpressionValueBuilder sourceValue,
                @Nonnull ExpressionValueBuilder targetValue)
        {
            super(elementContext);
            this.operator = Objects.requireNonNull(operator);
            this.sourceValue = Objects.requireNonNull(sourceValue);
            this.targetValue = Objects.requireNonNull(targetValue);
        }

        @Nonnull
        @Override
        public OperatorCriteria build()
        {
            return new OperatorCriteria(
                    this.elementContext,
                    this.operator.build(),
                    this.sourceValue.build(),
                    this.targetValue.build());
        }
    }
}
