package cool.klass.model.converter.compiler.state.criteria;

import javax.annotation.Nonnull;

public interface AntlrCriteriaVisitor
{
    void visitAll(@Nonnull AllAntlrCriteria allCriteria);

    void visitAnd(@Nonnull AntlrAndCriteria andCriteria);

    void visitOr(@Nonnull AntlrOrCriteria orCriteria);

    void visitOperator(@Nonnull OperatorAntlrCriteria operatorCriteria);

    void visitEdgePoint(@Nonnull EdgePointAntlrCriteria edgePointCriteria);
}
