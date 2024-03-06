package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface CriteriaVisitor
{
    void visitAll(@Nonnull AllCriteria allCriteria);

    void visitAnd(@Nonnull AndCriteria andCriteria);

    void visitOr(@Nonnull OrCriteria orCriteria);

    void visitOperator(@Nonnull OperatorCriteria operatorCriteria);

    void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria);
}
