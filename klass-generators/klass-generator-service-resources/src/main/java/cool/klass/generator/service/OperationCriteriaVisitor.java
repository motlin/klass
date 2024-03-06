package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import cool.klass.model.meta.domain.api.operator.Operator;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;

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
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
        this.stringBuilder.append(this.finderName).append(".all()");
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        andCriteria.getLeft().visit(this);
        this.stringBuilder.append(".and(");
        andCriteria.getRight().visit(this);
        this.stringBuilder.append(")");
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        orCriteria.getLeft().visit(this);
        this.stringBuilder.append(".or(");
        orCriteria.getRight().visit(this);
        this.stringBuilder.append(")");
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
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

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        MemberReferencePath memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        memberExpressionValue.visit(new OperationExpressionValueVisitor(
                this.finderName,
                this.stringBuilder));
        this.stringBuilder.append(".equalsEdgePoint()");
    }
}
