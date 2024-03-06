package cool.klass.model.converter.compiler.state;

import java.util.Set;

import cool.klass.model.converter.compiler.state.criteria.AllAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.AntlrAndCriteria;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteriaVisitor;
import cool.klass.model.converter.compiler.state.criteria.AntlrOrCriteria;
import cool.klass.model.converter.compiler.state.criteria.EdgePointAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.OperatorAntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrMemberReferencePath;

public class UnreferencedPrivatePropertiesCriteriaVisitor
        implements AntlrCriteriaVisitor
{
    private final UnreferencedPrivatePropertiesExpressionValueVisitor expressionValueVisitor = new UnreferencedPrivatePropertiesExpressionValueVisitor();

    public Set<AntlrAssociationEnd> getAssociationEndsReferencedByCriteria()
    {
        return this.expressionValueVisitor.getAssociationEndsReferencedByCriteria();
    }

    public Set<AntlrDataTypeProperty<?>> getDataTypePropertiesReferencedByCriteria()
    {
        return this.expressionValueVisitor.getDataTypePropertiesReferencedByCriteria();
    }

    @Override
    public void visitEdgePoint(EdgePointAntlrCriteria edgePointCriteria)
    {
        AntlrMemberReferencePath memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        memberExpressionValue.visit(this.expressionValueVisitor);
    }

    @Override
    public void visitOperator(OperatorAntlrCriteria operatorCriteria)
    {
        AntlrExpressionValue sourceValue = operatorCriteria.getSourceValue();
        AntlrExpressionValue targetValue = operatorCriteria.getTargetValue();
        sourceValue.visit(this.expressionValueVisitor);
        targetValue.visit(this.expressionValueVisitor);
    }

    @Override
    public void visitAll(AllAntlrCriteria allCriteria)
    {
    }

    @Override
    public void visitAnd(AntlrAndCriteria andCriteria)
    {
    }

    @Override
    public void visitOr(AntlrOrCriteria orCriteria)
    {
    }
}
