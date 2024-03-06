package cool.klass.model.meta.domain.api.operator;

public interface StringOperator extends Operator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitString(this);
    }
}
