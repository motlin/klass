package cool.klass.model.meta.domain.api.operator;

public interface InequalityOperator extends Operator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitInequality(this);
    }
}
