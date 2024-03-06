package cool.klass.model.meta.domain.api.value;

public interface IThisMemberReferencePath extends IMemberReferencePath
{
    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitThisMember(this);
    }
}
