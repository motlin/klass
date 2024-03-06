package cool.klass.model.meta.domain.api.value;

public interface ThisMemberReferencePath extends MemberReferencePath
{
    @Override
    default void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitThisMember(this);
    }
}
