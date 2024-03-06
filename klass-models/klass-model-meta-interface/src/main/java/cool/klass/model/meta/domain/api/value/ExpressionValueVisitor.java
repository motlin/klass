package cool.klass.model.meta.domain.api.value;

import cool.klass.model.meta.domain.api.value.literal.IIntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.ILiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.IStringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.IUserLiteral;

public interface ExpressionValueVisitor
{
    void visitTypeMember(ITypeMemberReferencePath typeMemberExpressionValue);

    void visitThisMember(IThisMemberReferencePath thisMemberExpressionValue);

    void visitVariableReference(IVariableReference variableReference);

    void visitIntegerLiteral(IIntegerLiteralValue integerLiteralValue);

    void visitStringLiteral(IStringLiteralValue stringLiteralValue);

    void visitLiteralList(ILiteralListValue literalListValue);

    void visitUserLiteral(IUserLiteral userLiteral);
}
