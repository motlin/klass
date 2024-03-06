package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import klass.model.meta.domain.MemberReferencePathAbstract;
import org.eclipse.collections.api.list.ImmutableList;

public class BootstrapExpressionValueVisitor implements ExpressionValueVisitor
{
    private klass.model.meta.domain.ExpressionValue bootstrappedExpressionValue;

    public static klass.model.meta.domain.ExpressionValue convert(ExpressionValue expressionValue)
    {
        BootstrapExpressionValueVisitor visitor = new BootstrapExpressionValueVisitor();
        expressionValue.visit(visitor);
        return visitor.getResult();
    }

    private klass.model.meta.domain.ExpressionValue getResult()
    {
        return Objects.requireNonNull(this.bootstrappedExpressionValue);
    }

    @Override
    public void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue)
    {
        this.handleMemberReferencePath(
                typeMemberExpressionValue,
                new klass.model.meta.domain.TypeMemberReferencePath());
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        this.handleMemberReferencePath(
                thisMemberExpressionValue,
                new klass.model.meta.domain.ThisMemberReferencePath());
    }

    private void handleMemberReferencePath(
            MemberReferencePath memberExpressionValue,
            MemberReferencePathAbstract bootstrappedMemberReferencePath)
    {
        Klass                         klass           = memberExpressionValue.getKlass();
        ImmutableList<AssociationEnd> associationEnds = memberExpressionValue.getAssociationEnds();
        DataTypeProperty              property        = memberExpressionValue.getProperty();

        bootstrappedMemberReferencePath.setClassName(klass.getName());
        bootstrappedMemberReferencePath.setPropertyClassName(property.getOwningClassifier().getName());
        bootstrappedMemberReferencePath.setPropertyName(property.getName());

        bootstrappedMemberReferencePath.insert();

        this.bootstrappedExpressionValue = bootstrappedMemberReferencePath;
    }

    @Override
    public void visitVariableReference(@Nonnull VariableReference variableReference)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitVariableReference() not implemented yet");
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
}
