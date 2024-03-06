package cool.klass.model.meta.domain.value;

public interface ExpressionValueVisitor
{
    void visitTypeMember(TypeMemberExpressionValue typeMemberExpressionValue);

    void visitThisMember(ThisMemberExpressionValue thisMemberExpressionValue);
}
