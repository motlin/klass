package cool.klass.model.meta.domain.api.value;

public interface TypeMemberReferencePath extends MemberReferencePath
{
    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitTypeMember(this);
    }
}
