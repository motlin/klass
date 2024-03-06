package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;

public interface ExpressionValueVisitor
{
    void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue);

    void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue);

    void visitVariableReference(@Nonnull VariableReference variableReference);

    void visitIntegerLiteral(@Nonnull IntegerLiteralValue integerLiteralValue);

    void visitStringLiteral(@Nonnull StringLiteralValue stringLiteralValue);

    void visitLiteralList(@Nonnull LiteralListValue literalListValue);

    void visitUserLiteral(@Nonnull UserLiteral userLiteral);
}
