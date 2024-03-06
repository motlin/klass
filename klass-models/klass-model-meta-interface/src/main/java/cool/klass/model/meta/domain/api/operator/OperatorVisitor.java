package cool.klass.model.meta.domain.api.operator;

public interface OperatorVisitor
{
    void visitEquality(IEqualityOperator equalityOperator);

    void visitInequality(IInequalityOperator inequalityOperator);

    void visitIn(IInOperator inOperator);

    void visitString(IStringOperator stringOperator);
}
