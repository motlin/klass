package cool.klass.generator.service;

import java.util.Objects;

import cool.klass.model.meta.domain.criteria.AllCriteria;
import cool.klass.model.meta.domain.criteria.AndCriteria;
import cool.klass.model.meta.domain.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.criteria.OrCriteria;
import cool.klass.model.meta.domain.operator.Operator;
import cool.klass.model.meta.domain.value.ExpressionValue;

public class OperationCriteriaVisitor implements CriteriaVisitor
{
    private final String        finderName;
    private final StringBuilder stringBuilder;

    public OperationCriteriaVisitor(String finderName, StringBuilder stringBuilder)
    {
        this.finderName = Objects.requireNonNull(finderName);
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
    }

    @Override
    public void visitAll(AllCriteria allCriteria)
    {
        this.stringBuilder.append(this.finderName).append(".all()");
    }

    @Override
    public void visitAnd(AndCriteria andCriteria)
    {
        andCriteria.getLeft().visit(this);
        this.stringBuilder.append(".and(");
        andCriteria.getRight().visit(this);
        this.stringBuilder.append(")");
    }

    @Override
    public void visitOr(OrCriteria orCriteria)
    {
        orCriteria.getLeft().visit(this);
        this.stringBuilder.append(".or(");
        orCriteria.getRight().visit(this);
        this.stringBuilder.append(")");
    }

    @Override
    public void visitOperator(OperatorCriteria operatorCriteria)
    {
        ExpressionValue sourceValue = operatorCriteria.getSourceValue();
        Operator        operator    = operatorCriteria.getOperator();
        ExpressionValue targetValue = operatorCriteria.getTargetValue();

        sourceValue.visit(new OperationExpressionValueVisitor(
                this.finderName,
                this.stringBuilder));

        operator.visit(new OperationOperatorVisitor(this.stringBuilder));

        targetValue.visit(new OperationExpressionValueVisitor(
                this.finderName,
                this.stringBuilder));

        this.stringBuilder.append(")");
    }
}
