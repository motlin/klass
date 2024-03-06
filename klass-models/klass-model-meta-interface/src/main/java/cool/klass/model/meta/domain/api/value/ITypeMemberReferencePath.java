package cool.klass.model.meta.domain.api.value;

public interface ITypeMemberReferencePath extends IMemberReferencePath
{
    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitTypeMember(this);
    }
}
