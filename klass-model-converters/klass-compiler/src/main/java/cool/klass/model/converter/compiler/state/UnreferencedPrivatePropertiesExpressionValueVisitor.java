package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValueVisitor;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.converter.compiler.state.value.AntlrTypeMemberReferencePath;
import cool.klass.model.converter.compiler.state.value.AntlrVariableReference;
import cool.klass.model.converter.compiler.state.value.literal.AntlrBooleanLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrFloatingPointLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrIntegerLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrLiteralListValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrNullLiteral;
import cool.klass.model.converter.compiler.state.value.literal.AntlrStringLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrUserLiteral;
import org.eclipse.collections.api.list.ImmutableList;

public class UnreferencedPrivatePropertiesExpressionValueVisitor
        implements AntlrExpressionValueVisitor
{
    private final Set<AntlrAssociationEnd>      associationEndsReferencedByCriteria    = new LinkedHashSet<>();
    private final Set<AntlrDataTypeProperty<?>> dataTypePropertiesReferencedByCriteria = new LinkedHashSet<>();

    public Set<AntlrAssociationEnd> getAssociationEndsReferencedByCriteria()
    {
        return this.associationEndsReferencedByCriteria;
    }

    public Set<AntlrDataTypeProperty<?>> getDataTypePropertiesReferencedByCriteria()
    {
        return this.dataTypePropertiesReferencedByCriteria;
    }

    @Override
    public void visitTypeMember(@Nonnull AntlrTypeMemberReferencePath typeMemberExpressionValue)
    {
        ImmutableList<AntlrAssociationEnd> associationEnds = typeMemberExpressionValue.getAssociationEnds();
        this.associationEndsReferencedByCriteria.addAll(associationEnds.castToList());
        AntlrDataTypeProperty<?> dataTypeProperty = typeMemberExpressionValue.getDataTypeProperty();
        this.dataTypePropertiesReferencedByCriteria.add(dataTypeProperty);
    }

    @Override
    public void visitThisMember(@Nonnull AntlrThisMemberReferencePath thisMemberExpressionValue)
    {
        ImmutableList<AntlrAssociationEnd> associationEnds = thisMemberExpressionValue.getAssociationEnds();
        this.associationEndsReferencedByCriteria.addAll(associationEnds.castToList());
        AntlrDataTypeProperty<?> dataTypeProperty = thisMemberExpressionValue.getDataTypeProperty();
        this.dataTypePropertiesReferencedByCriteria.add(dataTypeProperty);
    }

    @Override
    public void visitVariableReference(@Nonnull AntlrVariableReference variableReference)
    {
    }

    @Override
    public void visitBooleanLiteral(@Nonnull AntlrBooleanLiteralValue booleanLiteralValue)
    {
    }

    @Override
    public void visitIntegerLiteral(@Nonnull AntlrIntegerLiteralValue integerLiteralValue)
    {
    }

    @Override
    public void visitFloatingPointLiteral(@Nonnull AntlrFloatingPointLiteralValue floatingPointLiteral)
    {
    }

    @Override
    public void visitStringLiteral(@Nonnull AntlrStringLiteralValue stringLiteralValue)
    {
    }

    @Override
    public void visitLiteralList(@Nonnull AntlrLiteralListValue literalListValue)
    {
    }

    @Override
    public void visitUserLiteral(@Nonnull AntlrUserLiteral userLiteral)
    {
    }

    @Override
    public void visitNullLiteral(@Nonnull AntlrNullLiteral nullLiteral)
    {
    }
}
