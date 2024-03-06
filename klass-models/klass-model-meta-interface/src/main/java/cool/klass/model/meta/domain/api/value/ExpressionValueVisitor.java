package cool.klass.model.meta.domain.api.value;

import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;

public interface ExpressionValueVisitor
{
    void visitTypeMember(TypeMemberReferencePath typeMemberExpressionValue);

    void visitThisMember(ThisMemberReferencePath thisMemberExpressionValue);

    void visitVariableReference(VariableReference variableReference);

    void visitIntegerLiteral(IntegerLiteralValue integerLiteralValue);

    void visitStringLiteral(StringLiteralValue stringLiteralValue);

    void visitLiteralList(LiteralListValue literalListValue);

    void visitUserLiteral(UserLiteral userLiteral);
}
