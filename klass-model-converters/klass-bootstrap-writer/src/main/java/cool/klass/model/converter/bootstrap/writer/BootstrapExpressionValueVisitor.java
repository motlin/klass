package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;

public class BootstrapExpressionValueVisitor implements ExpressionValueVisitor
{
    private final ExpressionValue                         expressionValue;
    private       klass.model.meta.domain.ExpressionValue bootstrappedExpressionValue;

    public BootstrapExpressionValueVisitor(ExpressionValue expressionValue)
    {
        this.expressionValue = Objects.requireNonNull(expressionValue);
    }

    public static klass.model.meta.domain.ExpressionValue convert(ExpressionValue expressionValue)
    {
        BootstrapExpressionValueVisitor visitor = new BootstrapExpressionValueVisitor(expressionValue);
        expressionValue.visit(visitor);
        return visitor.getResult();
    }

    private klass.model.meta.domain.ExpressionValue getResult()
    {
        return this.bootstrappedExpressionValue;
    }

    @Override
    public void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue)
    {
        Objects.requireNonNull(typeMemberExpressionValue);
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        Objects.requireNonNull(thisMemberExpressionValue);
    }

    @Override
    public void visitVariableReference(@Nonnull VariableReference variableReference)
    {
        Objects.requireNonNull(variableReference);
    }

    @Override
    public void visitIntegerLiteral(@Nonnull IntegerLiteralValue integerLiteralValue)
    {
        Objects.requireNonNull(integerLiteralValue);
    }

    @Override
    public void visitStringLiteral(@Nonnull StringLiteralValue stringLiteralValue)
    {
        Objects.requireNonNull(stringLiteralValue);
    }

    @Override
    public void visitLiteralList(@Nonnull LiteralListValue literalListValue)
    {
        Objects.requireNonNull(literalListValue);
    }

    @Override
    public void visitUserLiteral(@Nonnull UserLiteral userLiteral)
    {
        Objects.requireNonNull(userLiteral);
    }
}
