package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import cool.klass.model.meta.domain.operator.Operator;
import cool.klass.model.meta.domain.operator.Operator.OperatorBuilder;
import cool.klass.model.meta.domain.value.ExpressionValue;
import cool.klass.model.meta.domain.value.ExpressionValue.ExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class OperatorCriteria extends Criteria
{
    private final Operator        operator;
    private final ExpressionValue sourceValue;
    private final ExpressionValue targetValue;

    protected OperatorCriteria(
            ParserRuleContext elementContext,
            Operator operator,
            ExpressionValue sourceValue,
            ExpressionValue targetValue)
    {
        super(elementContext);
        this.operator = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    public static final class OperatorCriteriaBuilder extends CriteriaBuilder
    {
        private final OperatorBuilder        operator;
        private final ExpressionValueBuilder sourceValue;
        private final ExpressionValueBuilder targetValue;

        public OperatorCriteriaBuilder(
                ParserRuleContext elementContext,
                OperatorBuilder operator,
                ExpressionValueBuilder sourceValue,
                ExpressionValueBuilder targetValue)
        {
            super(elementContext);
            this.operator = Objects.requireNonNull(operator);
            this.sourceValue = Objects.requireNonNull(sourceValue);
            this.targetValue = Objects.requireNonNull(targetValue);
        }

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
