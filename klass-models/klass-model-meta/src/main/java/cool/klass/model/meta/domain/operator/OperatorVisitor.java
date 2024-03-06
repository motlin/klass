package cool.klass.model.meta.domain.operator;

public interface OperatorVisitor
{
    void visitEquality(EqualityOperator equalityOperator);

    void visitInequality(InequalityOperator inequalityOperator);

    void visitIn(InOperator inOperator);

    void visitString(StringOperator stringOperator);
}
