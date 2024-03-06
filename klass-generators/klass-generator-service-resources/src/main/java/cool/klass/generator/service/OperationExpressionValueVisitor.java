package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.api.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralValue;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;
import cool.klass.model.meta.domain.api.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.api.value.literal.UserLiteral;
import cool.klass.model.meta.domain.api.visitor.PrimitiveToJavaTypeVisitor;
import org.eclipse.collections.api.list.ImmutableList;

public class OperationExpressionValueVisitor implements ExpressionValueVisitor
{
    private final String        finderName;
    private final StringBuilder stringBuilder;

    public OperationExpressionValueVisitor(@Nonnull String finderName, @Nonnull StringBuilder stringBuilder)
    {
        this.finderName    = Objects.requireNonNull(finderName);
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
    }

    @Override
    public void visitTypeMember(@Nonnull TypeMemberReferencePath typeMemberExpressionValue)
    {
        ImmutableList<AssociationEnd> associationEnds = typeMemberExpressionValue.getAssociationEnds();

        String associationEndsString = associationEnds.isEmpty()
                ? ""
                : "." + associationEnds
                        .collect(NamedElement::getName)
                        .collect(string -> string + "()")
                        .makeString(".");

        String attribute = String.format(
                "%sFinder%s.%s()",
                typeMemberExpressionValue.getKlass().getName(),
                associationEndsString,
                typeMemberExpressionValue.getProperty().getName());
        this.stringBuilder.append(attribute);
    }

    @Override
    public void visitThisMember(@Nonnull ThisMemberReferencePath thisMemberExpressionValue)
    {
        ImmutableList<AssociationEnd> associationEnds = thisMemberExpressionValue.getAssociationEnds();

        String associationEndsString = associationEnds.isEmpty()
                ? ""
                : "." + associationEnds
                        .collect(NamedElement::getName)
                        .collect(string -> string + "()")
                        .makeString(".");

        String attribute = String.format(
                "%s%s.%s()",
                this.finderName,
                associationEndsString,
                thisMemberExpressionValue.getProperty().getName());
        this.stringBuilder.append(attribute);
    }

    @Override
    public void visitVariableReference(@Nonnull VariableReference variableReference)
    {
        Parameter    parameter    = variableReference.getParameter();
        DataType     dataType     = parameter.getType();
        Multiplicity multiplicity = parameter.getMultiplicity();

        if (dataType instanceof Enumeration)
        {
            this.stringBuilder.append(parameter.getName());
            return;
        }

        PrimitiveType primitiveType = (PrimitiveType) dataType;
        if (multiplicity.isToOne())
        {
            primitiveType.visit(new ReladomoPrimitiveVisitor(this.stringBuilder, parameter.getName()));
            return;
        }

        primitiveType.visit(new PrimitiveSetVisitor(this.stringBuilder, parameter.getName()));
    }

    @Override
    public void visitIntegerLiteral(@Nonnull IntegerLiteralValue integerLiteralValue)
    {
        this.stringBuilder.append(integerLiteralValue.getValue());
    }

    @Override
    public void visitStringLiteral(@Nonnull StringLiteralValue stringLiteralValue)
    {
        this.stringBuilder.append('"');
        this.stringBuilder.append(stringLiteralValue.getValue());
        this.stringBuilder.append('"');
    }

    @Override
    public void visitLiteralList(@Nonnull LiteralListValue literalListValue)
    {
        Type type = literalListValue.getType();
        this.stringBuilder.append(this.getType(type));
        this.stringBuilder.append("Sets.immutable.with(");
        this.stringBuilder.append(literalListValue.getLiteralValues().collect(this::getLiteralString).makeString());
        this.stringBuilder.append(")");
    }

    private String getType(Type type)
    {
        if (type instanceof PrimitiveType)
        {
            PrimitiveType primitiveType = (PrimitiveType) type;
            return PrimitiveToJavaTypeVisitor.getJavaType(primitiveType);
        }
        throw new AssertionError();
    }

    @Override
    public void visitUserLiteral(@Nonnull UserLiteral userLiteral)
    {
        this.stringBuilder.append("userPrincipalName");
    }

    @Override
    public void visitNullLiteral(@Nonnull NullLiteral nullLiteral)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitNullLiteral() not implemented yet");
    }

    @Nonnull
    private String getLiteralString(@Nonnull LiteralValue literalValue)
    {
        StringBuilder stringBuilder = new StringBuilder();
        literalValue.visit(new OperationExpressionValueVisitor(this.finderName, stringBuilder));
        return stringBuilder.toString();
    }
}
