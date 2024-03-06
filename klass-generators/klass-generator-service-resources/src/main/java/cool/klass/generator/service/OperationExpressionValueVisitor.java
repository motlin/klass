package cool.klass.generator.service;

import java.util.Objects;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.domain.service.url.UrlParameter;
import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.value.ThisMemberExpressionValue;
import cool.klass.model.meta.domain.value.TypeMemberExpressionValue;
import cool.klass.model.meta.domain.value.VariableReference;
import cool.klass.model.meta.domain.value.literal.IntegerLiteralValue;
import cool.klass.model.meta.domain.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.value.literal.LiteralValue;
import cool.klass.model.meta.domain.value.literal.StringLiteralValue;
import cool.klass.model.meta.domain.value.literal.UserLiteral;

public class OperationExpressionValueVisitor implements ExpressionValueVisitor
{
    private final String        finderName;
    private final StringBuilder stringBuilder;

    public OperationExpressionValueVisitor(String finderName, StringBuilder stringBuilder)
    {
        this.finderName = Objects.requireNonNull(finderName);
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
    }

    @Override
    public void visitTypeMember(TypeMemberExpressionValue typeMemberExpressionValue)
    {
        String attribute = String.format(
                "%sFinder.%s()",
                typeMemberExpressionValue.getKlass().getName(),
                typeMemberExpressionValue.getProperty().getName());
        this.stringBuilder.append(attribute);
    }

    @Override
    public void visitThisMember(ThisMemberExpressionValue thisMemberExpressionValue)
    {
        String attribute = String.format(
                "%s.%s()",
                this.finderName,
                thisMemberExpressionValue.getProperty().getName());
        this.stringBuilder.append(attribute);
    }

    @Override
    public void visitVariableReference(VariableReference variableReference)
    {
        UrlParameter urlParameter = variableReference.getUrlParameter();
        DataType     dataType     = urlParameter.getType();
        Multiplicity multiplicity = urlParameter.getMultiplicity();

        if (dataType instanceof Enumeration)
        {
            this.stringBuilder.append(urlParameter.getName());
            return;
        }
        if (multiplicity.isToOne())
        {
            this.stringBuilder.append(urlParameter.getName());
            return;
        }

        PrimitiveType primitiveType = (PrimitiveType) dataType;
        primitiveType.visit(new PrimitiveSetVisitor(this.stringBuilder, urlParameter.getName()));
    }

    @Override
    public void visitIntegerLiteral(IntegerLiteralValue integerLiteralValue)
    {
        this.stringBuilder.append(integerLiteralValue.getValue());
    }

    @Override
    public void visitStringLiteral(StringLiteralValue stringLiteralValue)
    {
        this.stringBuilder.append('"');
        this.stringBuilder.append(stringLiteralValue.getValue());
        this.stringBuilder.append('"');
    }

    @Override
    public void visitLiteralList(LiteralListValue literalListValue)
    {
        this.stringBuilder.append(literalListValue.getType().getName());
        this.stringBuilder.append("Sets.immutable.with(");
        this.stringBuilder.append(literalListValue.getLiteralValues().collect(this::getLiteralString).makeString());
        this.stringBuilder.append(")");
    }

    @Override
    public void visitUserLiteral(UserLiteral userLiteral)
    {
        this.stringBuilder.append("userPrincipalName");
    }

    private String getLiteralString(LiteralValue literalValue)
    {
        StringBuilder stringBuilder = new StringBuilder();
        literalValue.visit(new OperationExpressionValueVisitor(this.finderName, stringBuilder));
        return stringBuilder.toString();
    }
}
