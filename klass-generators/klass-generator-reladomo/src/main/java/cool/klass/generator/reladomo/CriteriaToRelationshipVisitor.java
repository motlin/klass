package cool.klass.generator.reladomo;

import cool.klass.model.meta.domain.criteria.AndCriteria;
import cool.klass.model.meta.domain.criteria.BinaryCriteria;
import cool.klass.model.meta.domain.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.criteria.OrCriteria;

public class CriteriaToRelationshipVisitor implements CriteriaVisitor
{
    private final StringBuilder stringBuilder;

    public CriteriaToRelationshipVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitAnd(AndCriteria andCriteria)
    {
        andCriteria.getLeft().visit(this);
        this.stringBuilder.append(" and ");
        andCriteria.getRight().visit(this);
    }

    @Override
    public void visitOr(OrCriteria orCriteria)
    {
        orCriteria.getLeft().visit(this);
        this.stringBuilder.append(" or ");
        orCriteria.getRight().visit(this);
    }

    @Override
    public void visitOperator(OperatorCriteria operatorCriteria)
    {
        operatorCriteria.getSourceValue().visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
        operatorCriteria.getOperator().visit(new OperatorToRelationshipVisitor(this.stringBuilder));
        operatorCriteria.getTargetValue().visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
    }

    @Override
    public void visitBinary(BinaryCriteria binaryCriteria)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitBinary() not implemented yet");
    }
}
