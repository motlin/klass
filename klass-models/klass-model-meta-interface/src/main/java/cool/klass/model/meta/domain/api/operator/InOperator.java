package cool.klass.model.meta.domain.api.operator;

public interface InOperator extends Operator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitIn(this);
    }
}
