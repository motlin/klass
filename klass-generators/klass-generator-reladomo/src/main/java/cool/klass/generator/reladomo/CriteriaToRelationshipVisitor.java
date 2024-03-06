package cool.klass.generator.reladomo;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;

public class CriteriaToRelationshipVisitor implements CriteriaVisitor
{
    private final StringBuilder stringBuilder;

    public CriteriaToRelationshipVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".visitAll() not implemented yet");
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        andCriteria.getLeft().visit(this);
        this.stringBuilder.append(" and ");
        andCriteria.getRight().visit(this);
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        orCriteria.getLeft().visit(this);
        this.stringBuilder.append(" or ");
        orCriteria.getRight().visit(this);
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
    {
        operatorCriteria.getSourceValue().visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
        operatorCriteria.getOperator().visit(new OperatorToRelationshipVisitor(this.stringBuilder));
        operatorCriteria.getTargetValue().visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitEdgePoint() not implemented yet");
    }
}
