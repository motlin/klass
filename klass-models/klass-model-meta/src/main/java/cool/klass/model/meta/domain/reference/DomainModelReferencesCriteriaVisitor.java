package cool.klass.model.meta.domain.reference;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;

public class DomainModelReferencesCriteriaVisitor
        implements CriteriaVisitor
{
    private final DomainModelReferences domainModelReferences;

    public DomainModelReferencesCriteriaVisitor(DomainModelReferences domainModelReferences)
    {
        this.domainModelReferences = domainModelReferences;
    }

    @Override
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
        // Deliberately empty
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
        sourceValue.visit(new DomainModelReferencesExpressionValueVisitor(this.domainModelReferences));
        targetValue.visit(new DomainModelReferencesExpressionValueVisitor(this.domainModelReferences));
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        MemberReferencePath memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        memberExpressionValue.visit(new DomainModelReferencesExpressionValueVisitor(this.domainModelReferences));
    }
}
