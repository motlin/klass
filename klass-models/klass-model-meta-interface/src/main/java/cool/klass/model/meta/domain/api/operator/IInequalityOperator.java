package cool.klass.model.meta.domain.api.operator;

public interface IInequalityOperator extends IOperator
{
    @Override
    default void visit(OperatorVisitor visitor)
    {
        visitor.visitInequality(this);
    }
}
