package cool.klass.model.meta.domain.api.operator;

import javax.annotation.Nonnull;

public interface OperatorVisitor
{
    void visitEquality(@Nonnull EqualityOperator equalityOperator);

    void visitInequality(@Nonnull InequalityOperator inequalityOperator);

    void visitIn(@Nonnull InOperator inOperator);

    void visitString(@Nonnull StringOperator stringOperator);
}
