package cool.klass.generator.reladomo;

import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.value.ThisMemberExpressionValue;
import cool.klass.model.meta.domain.value.TypeMemberExpressionValue;

public class ExpressionValueToRelationshipVisitor implements ExpressionValueVisitor
{
    private final StringBuilder stringBuilder;

    public ExpressionValueToRelationshipVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitTypeMember(TypeMemberExpressionValue typeMemberExpressionValue)
    {
        this.stringBuilder.append(typeMemberExpressionValue.getKlass().getName());
        this.stringBuilder.append('.');
        this.stringBuilder.append(typeMemberExpressionValue.getProperty().getName());
    }

    @Override
    public void visitThisMember(ThisMemberExpressionValue thisMemberExpressionValue)
    {
        this.stringBuilder.append("this.");
        this.stringBuilder.append(thisMemberExpressionValue.getProperty().getName());
    }
}
