package cool.klass.model.meta.domain.api.operator;

public interface EqualityOperator extends Operator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitEquality(this);
    }
}
