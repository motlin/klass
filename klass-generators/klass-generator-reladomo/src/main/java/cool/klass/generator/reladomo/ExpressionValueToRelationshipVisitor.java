package cool.klass.generator.reladomo;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.api.value.literal.BooleanLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;

class ExpressionValueToRelationshipVisitor
        implements ExpressionValueVisitor
{
    @Nonnull
    private final StringBuilder stringBuilder;
    private final boolean       reverse;

    ExpressionValueToRelationshipVisitor(@Nonnull StringBuilder stringBuilder, boolean reverse)
    {
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
        this.reverse       = reverse;
    }

    @Override
    public void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue)
    {
        if (this.reverse)
        {
            this.stringBuilder.append("this.");
            this.stringBuilder.append(typeMemberExpressionValue.getProperty().getName());
        }
        else
        {
            this.stringBuilder.append(typeMemberExpressionValue.getKlass().getName());
            this.stringBuilder.append('.');
            this.stringBuilder.append(typeMemberExpressionValue.getProperty().getName());
        }
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        if (this.reverse)
        {
            this.stringBuilder.append(thisMemberExpressionValue.getKlass().getName());
            this.stringBuilder.append('.');
            this.stringBuilder.append(thisMemberExpressionValue.getProperty().getName());
        }
        else
        {
            this.stringBuilder.append("this.");
            this.stringBuilder.append(thisMemberExpressionValue.getProperty().getName());
        }
    }

    @Override
    public void visitVariableReference(@Nonnull VariableReference variableReference)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitVariableReference() not implemented yet");
    }

    @Override
    public void visitBooleanLiteral(@Nonnull BooleanLiteralValue booleanLiteralValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitBooleanLiteral() not implemented yet");
    }

    @Override
    public void visitIntegerLiteral(@Nonnull IntegerLiteralValue integerLiteralValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitIntegerLiteral() not implemented yet");
    }

    @Override
    public void visitStringLiteral(@Nonnull StringLiteralValue stringLiteralValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitStringLiteral() not implemented yet");
    }

    @Override
    public void visitLiteralList(@Nonnull LiteralListValue literalListValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitLiteralList() not implemented yet");
    }

    @Override
    public void visitUserLiteral(@Nonnull UserLiteral userLiteral)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitUserLiteral() not implemented yet");
    }

    @Override
    public void visitNullLiteral(@Nonnull NullLiteral nullLiteral)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitNullLiteral() not implemented yet");
    }
}
