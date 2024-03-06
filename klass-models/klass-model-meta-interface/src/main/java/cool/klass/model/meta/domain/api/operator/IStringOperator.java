package cool.klass.model.meta.domain.api.operator;

public interface IStringOperator extends IOperator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitString(this);
    }
}
