package cool.klass.model.meta.domain.api.criteria;

import javax.annotation.Nonnull;

public interface CriteriaVisitor
{
    void visitAll(@Nonnull IAllCriteria allCriteria);

    void visitAnd(@Nonnull IAndCriteria andCriteria);

    void visitOr(@Nonnull IOrCriteria orCriteria);

    void visitOperator(@Nonnull IOperatorCriteria operatorCriteria);

    void visitEdgePoint(@Nonnull IEdgePointCriteria edgePointCriteria);
}
