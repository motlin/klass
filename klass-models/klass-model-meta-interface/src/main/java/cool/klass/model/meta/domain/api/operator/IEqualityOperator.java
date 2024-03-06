package cool.klass.model.meta.domain.api.operator;

public interface IEqualityOperator extends IOperator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitEquality(this);
    }
}
