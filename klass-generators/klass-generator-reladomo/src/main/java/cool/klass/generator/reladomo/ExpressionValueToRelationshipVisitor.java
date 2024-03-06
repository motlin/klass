package cool.klass.generator.reladomo;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.value.VariableReference;
import cool.klass.model.meta.domain.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.value.literal.UserLiteral;

public class ExpressionValueToRelationshipVisitor implements ExpressionValueVisitor
{
    private final StringBuilder stringBuilder;

    public ExpressionValueToRelationshipVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue)
    {
        this.stringBuilder.append(typeMemberExpressionValue.getKlass().getName());
        this.stringBuilder.append('.');
        this.stringBuilder.append(typeMemberExpressionValue.getProperty().getName());
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        this.stringBuilder.append("this.");
        this.stringBuilder.append(thisMemberExpressionValue.getProperty().getName());
    }

    @Override
    public void visitVariableReference(VariableReference variableReference)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitVariableReference() not implemented yet");
    }

    @Override
    public void visitIntegerLiteral(IntegerLiteralValue integerLiteralValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitIntegerLiteral() not implemented yet");
    }

    @Override
    public void visitStringLiteral(StringLiteralValue stringLiteralValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitStringLiteral() not implemented yet");
    }

    @Override
    public void visitLiteralList(LiteralListValue literalListValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitLiteralList() not implemented yet");
    }

    @Override
    public void visitUserLiteral(UserLiteral userLiteral)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitUserLiteral() not implemented yet");
    }
}
