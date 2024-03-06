package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;

public class BootstrapExpressionValueCriteriaVisitor
        implements CriteriaVisitor
{
    private final ExpressionValueVisitor expressionValueVisitor;

    public BootstrapExpressionValueCriteriaVisitor(ExpressionValueVisitor expressionValueVisitor)
    {
        this.expressionValueVisitor = Objects.requireNonNull(expressionValueVisitor);
    }

    @Override
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        andCriteria.getLeft().visit(this);
        andCriteria.getRight().visit(this);
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        orCriteria.getLeft().visit(this);
        orCriteria.getRight().visit(this);
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
    {
        ExpressionValue sourceValue = operatorCriteria.getSourceValue();
        ExpressionValue targetValue = operatorCriteria.getTargetValue();

        sourceValue.visit(this.expressionValueVisitor);
        targetValue.visit(this.expressionValueVisitor);
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        MemberReferencePath memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        memberExpressionValue.visit(this.expressionValueVisitor);
    }
}
