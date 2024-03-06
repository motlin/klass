package cool.klass.model.meta.domain.api.operator;

public interface IInOperator extends IOperator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitIn(this);
    }
}
