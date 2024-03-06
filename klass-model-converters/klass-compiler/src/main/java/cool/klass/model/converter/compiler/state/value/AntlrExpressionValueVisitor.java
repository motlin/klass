package cool.klass.model.converter.compiler.state.value;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.value.literal.AntlrBooleanLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrFloatingPointLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrIntegerLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrLiteralListValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrNullLiteral;
import cool.klass.model.converter.compiler.state.value.literal.AntlrStringLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrUserLiteral;

public interface AntlrExpressionValueVisitor
{
    void visitTypeMember(@Nonnull AntlrTypeMemberReferencePath typeMemberExpressionValue);

    void visitThisMember(@Nonnull AntlrThisMemberReferencePath thisMemberExpressionValue);

    void visitVariableReference(@Nonnull AntlrVariableReference variableReference);

    void visitBooleanLiteral(@Nonnull AntlrBooleanLiteralValue booleanLiteralValue);

    void visitIntegerLiteral(@Nonnull AntlrIntegerLiteralValue integerLiteralValue);

    void visitFloatingPointLiteral(@Nonnull AntlrFloatingPointLiteralValue floatingPointLiteral);

    void visitStringLiteral(@Nonnull AntlrStringLiteralValue stringLiteralValue);

    void visitLiteralList(@Nonnull AntlrLiteralListValue literalListValue);

    void visitUserLiteral(@Nonnull AntlrUserLiteral userLiteral);

    void visitNullLiteral(@Nonnull AntlrNullLiteral nullLiteral);
}
