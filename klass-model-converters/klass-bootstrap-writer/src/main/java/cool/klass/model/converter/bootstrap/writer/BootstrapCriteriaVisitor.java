package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.BinaryCriteria;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;
import org.eclipse.collections.api.map.ImmutableMap;

public class BootstrapCriteriaVisitor implements CriteriaVisitor
{
    private final ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter;

    private klass.model.meta.domain.Criteria bootstrappedCriteria;

    public BootstrapCriteriaVisitor(ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter)
    {
        this.bootstrappedParametersByParameter = bootstrappedParametersByParameter;
    }

    public static klass.model.meta.domain.Criteria convert(
            ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter,
            @Nonnull Criteria criteria)
    {
        BootstrapCriteriaVisitor visitor = new BootstrapCriteriaVisitor(bootstrappedParametersByParameter);
        criteria.visit(visitor);
        return visitor.getResult();
    }

    public klass.model.meta.domain.Criteria getResult()
    {
        return Objects.requireNonNull(this.bootstrappedCriteria);
    }

    @Override
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
        this.handleCriteria(new klass.model.meta.domain.AllCriteria(), allCriteria);
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        this.handleBinaryCriteria(new klass.model.meta.domain.AndCriteria(), andCriteria);
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        this.handleBinaryCriteria(new klass.model.meta.domain.OrCriteria(), orCriteria);
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
    {
        ExpressionValue sourceValue = operatorCriteria.getSourceValue();
        ExpressionValue targetValue = operatorCriteria.getTargetValue();

        klass.model.meta.domain.ExpressionValue bootstrappedSourceValue =
                BootstrapExpressionValueVisitor.convert(this.bootstrappedParametersByParameter, sourceValue);
        klass.model.meta.domain.ExpressionValue bootstrappedTargetValue =
                BootstrapExpressionValueVisitor.convert(this.bootstrappedParametersByParameter, targetValue);

        klass.model.meta.domain.OperatorCriteria bootstrappedCriteria = new klass.model.meta.domain.OperatorCriteria();
        KlassBootstrapWriter.handleElement(bootstrappedCriteria, operatorCriteria);

        bootstrappedCriteria.setOperator(operatorCriteria.getOperator().getOperatorText());
        long sourceValueId = bootstrappedSourceValue.getId();
        long targetValueId = bootstrappedTargetValue.getId();
        bootstrappedCriteria.setSourceExpressionId(sourceValueId);
        bootstrappedCriteria.setTargetExpressionId(targetValueId);
        bootstrappedCriteria.insert();
        this.bootstrappedCriteria = bootstrappedCriteria;
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        klass.model.meta.domain.MemberReferencePath bootstrappedMemberReferencePath = new klass.model.meta.domain.MemberReferencePath();
        bootstrappedMemberReferencePath.insert();

        MemberReferencePath memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        Klass               klass                 = memberExpressionValue.getKlass();
        DataTypeProperty    property              = memberExpressionValue.getProperty();

        bootstrappedMemberReferencePath.setClassName(klass.getName());
        bootstrappedMemberReferencePath.setPropertyClassName(property.getOwningClassifier().getName());
        bootstrappedMemberReferencePath.setPropertyName(property.getName());

        klass.model.meta.domain.EdgePointCriteria bootstrappedCriteria = new klass.model.meta.domain.EdgePointCriteria();
        KlassBootstrapWriter.handleElement(bootstrappedCriteria, edgePointCriteria);
        bootstrappedCriteria.setMemberReferencePathId(bootstrappedMemberReferencePath.getId());
        bootstrappedCriteria.insert();
        this.bootstrappedCriteria = bootstrappedCriteria;
    }

    private void handleBinaryCriteria(
            @Nonnull klass.model.meta.domain.BinaryCriteria bootstrappedCriteria,
            @Nonnull BinaryCriteria binaryCriteria)
    {
        klass.model.meta.domain.Criteria bootstrappedLeft = BootstrapCriteriaVisitor.convert(
                this.bootstrappedParametersByParameter,
                binaryCriteria.getLeft());
        klass.model.meta.domain.Criteria bootstrappedRight = BootstrapCriteriaVisitor.convert(
                this.bootstrappedParametersByParameter,
                binaryCriteria.getRight());

        bootstrappedCriteria.setLeft(bootstrappedLeft);
        bootstrappedCriteria.setRight(bootstrappedRight);

        this.handleCriteria(bootstrappedCriteria, binaryCriteria);
    }

    private void handleCriteria(
            @Nonnull klass.model.meta.domain.Criteria bootstrappedCriteria,
            @Nonnull Criteria criteria)
    {
        KlassBootstrapWriter.handleElement(bootstrappedCriteria, criteria);
        bootstrappedCriteria.insert();
        this.bootstrappedCriteria = bootstrappedCriteria;
    }
}
