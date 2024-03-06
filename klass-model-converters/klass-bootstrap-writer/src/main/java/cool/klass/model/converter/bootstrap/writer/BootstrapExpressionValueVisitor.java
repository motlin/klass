package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.api.value.literal.BooleanLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.FloatingPointLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import klass.model.meta.domain.MemberReferencePath;
import klass.model.meta.domain.NullLiteralFinder;
import klass.model.meta.domain.NullLiteralList;
import klass.model.meta.domain.UserLiteralFinder;
import klass.model.meta.domain.UserLiteralList;
import org.eclipse.collections.api.map.ImmutableMap;

public class BootstrapExpressionValueVisitor implements ExpressionValueVisitor
{
    private final ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter;

    private klass.model.meta.domain.ExpressionValue bootstrappedExpressionValue;

    public BootstrapExpressionValueVisitor(ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter)
    {
        this.bootstrappedParametersByParameter = bootstrappedParametersByParameter;
    }

    public static klass.model.meta.domain.ExpressionValue convert(
            ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameterId,
            @Nonnull ExpressionValue expressionValue)
    {
        var visitor = new BootstrapExpressionValueVisitor(bootstrappedParametersByParameterId);
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
        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        Klass            klass    = typeMemberExpressionValue.getKlass();
        DataTypeProperty property = typeMemberExpressionValue.getProperty();

        var bootstrappedMemberReferencePath = new MemberReferencePath();
        bootstrappedMemberReferencePath.setExpressionValueSuperClass(bootstrappedExpressionValue);
        bootstrappedMemberReferencePath.setClassName(klass.getName());
        bootstrappedMemberReferencePath.setPropertyClassName(property.getOwningClassifier().getName());
        bootstrappedMemberReferencePath.setPropertyName(property.getName());

        var bootstrappedTypeMemberReferencePath = new klass.model.meta.domain.TypeMemberReferencePath();
        bootstrappedTypeMemberReferencePath.setMemberReferencePathSuperClass(bootstrappedMemberReferencePath);

        this.bootstrappedExpressionValue = bootstrappedExpressionValue;
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        Klass            klass    = thisMemberExpressionValue.getKlass();
        DataTypeProperty property = thisMemberExpressionValue.getProperty();

        var bootstrappedMemberReferencePath = new MemberReferencePath();
        bootstrappedMemberReferencePath.setExpressionValueSuperClass(bootstrappedExpressionValue);
        bootstrappedMemberReferencePath.setClassName(klass.getName());
        bootstrappedMemberReferencePath.setPropertyClassName(property.getOwningClassifier().getName());
        bootstrappedMemberReferencePath.setPropertyName(property.getName());

        var bootstrappedThisMemberReferencePath = new klass.model.meta.domain.ThisMemberReferencePath();
        bootstrappedThisMemberReferencePath.setMemberReferencePathSuperClass(bootstrappedMemberReferencePath);

        this.bootstrappedExpressionValue = bootstrappedExpressionValue;
    }

    @Override
    public void visitVariableReference(@Nonnull VariableReference variableReference)
    {
        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        Parameter                         parameter             = variableReference.getParameter();
        klass.model.meta.domain.Parameter bootstrappedParameter = this.bootstrappedParametersByParameter.get(parameter);

        var bootstrappedVariableReference = new klass.model.meta.domain.VariableReference();
        bootstrappedVariableReference.setExpressionValueSuperClass(bootstrappedExpressionValue);
        bootstrappedVariableReference.setParameter(bootstrappedParameter);

        this.bootstrappedExpressionValue = bootstrappedExpressionValue;
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
    public void visitFloatingPointLiteral(@Nonnull FloatingPointLiteralValue floatingPointLiteralValue)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitFloatingPointLiteral() not implemented yet");
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
        UserLiteralList userLiterals = UserLiteralFinder.findMany(UserLiteralFinder.all());
        if (userLiterals.notEmpty())
        {
            this.bootstrappedExpressionValue = userLiterals.asEcList().getOnly().getExpressionValueSuperClass();
            return;
        }

        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        var bootstrappedUserLiteral = new klass.model.meta.domain.UserLiteral();
        bootstrappedUserLiteral.setExpressionValueSuperClass(bootstrappedExpressionValue);

        this.bootstrappedExpressionValue = bootstrappedExpressionValue;
    }

    @Override
    public void visitNullLiteral(@Nonnull NullLiteral nullLiteral)
    {
        NullLiteralList nullLiterals = NullLiteralFinder.findMany(NullLiteralFinder.all());
        if (nullLiterals.notEmpty())
        {
            this.bootstrappedExpressionValue = nullLiterals.asEcList().getOnly().getExpressionValueSuperClass();
            return;
        }

        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        var bootstrappedNullLiteral = new klass.model.meta.domain.NullLiteral();
        bootstrappedNullLiteral.setExpressionValueSuperClass(bootstrappedExpressionValue);

        this.bootstrappedExpressionValue = bootstrappedExpressionValue;
    }
}
