package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.meta.domain.criteria.OperatorCriteria.OperatorCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;

public class OperatorCriteria extends AntlrCriteria
{
    private final AntlrOperator        operator;
    private final AntlrExpressionValue sourceValue;
    private final AntlrExpressionValue targetValue;

    public OperatorCriteria(
            CriteriaOperatorContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            AntlrOperator operator,
            AntlrExpressionValue sourceValue,
            AntlrExpressionValue targetValue)
    {
        super(elementContext, compilationUnit, inferred);
        this.operator = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    @Override
    public CriteriaOperatorContext getElementContext()
    {
        return (CriteriaOperatorContext) super.getElementContext();
    }

    @Override
    public OperatorCriteriaBuilder build()
    {
        return new OperatorCriteriaBuilder(
                this.elementContext,
                this.operator.build(),
                this.sourceValue.build(),
                this.targetValue.build());
    }
}
