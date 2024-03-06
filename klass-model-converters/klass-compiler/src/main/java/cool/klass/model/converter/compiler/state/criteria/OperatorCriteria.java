package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.meta.domain.criteria.OperatorCriteria.OperatorCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;

public class OperatorCriteria extends AntlrCriteria
{
    @Nonnull
    private final AntlrOperator        operator;
    @Nonnull
    private final AntlrExpressionValue sourceValue;
    @Nonnull
    private final AntlrExpressionValue targetValue;

    public OperatorCriteria(
            @Nonnull CriteriaOperatorContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrOperator operator,
            @Nonnull AntlrExpressionValue sourceValue,
            @Nonnull AntlrExpressionValue targetValue)
    {
        super(elementContext, compilationUnit, inferred);
        this.operator = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    @Nonnull
    @Override
    public CriteriaOperatorContext getElementContext()
    {
        return (CriteriaOperatorContext) super.getElementContext();
    }

    @Nonnull
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
