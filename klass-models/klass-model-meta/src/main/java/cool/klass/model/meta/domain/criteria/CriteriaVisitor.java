package cool.klass.model.meta.domain.criteria;

public interface CriteriaVisitor
{
    void visitAll(AllCriteria allCriteria);

    void visitAnd(AndCriteria andCriteria);

    void visitOr(OrCriteria orCriteria);

    void visitOperator(OperatorCriteria operatorCriteria);

    void visitBinary(BinaryCriteria binaryCriteria);
}
