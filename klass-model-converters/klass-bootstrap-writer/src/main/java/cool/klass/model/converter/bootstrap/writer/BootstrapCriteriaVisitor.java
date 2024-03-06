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
        var visitor = new BootstrapCriteriaVisitor(bootstrappedParametersByParameter);
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
        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        bootstrappedCriteria.insert();

        var bootstrappedAllCriteria = new klass.model.meta.domain.AllCriteria();
        bootstrappedAllCriteria.setCriteriaSuperClass(bootstrappedCriteria);
        this.bootstrappedCriteria = bootstrappedCriteria;
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        var bootstrappedBinaryCriteria = this.handleBinaryCriteria(andCriteria);

        var bootstrappedAndCriteria = new klass.model.meta.domain.AndCriteria();
        bootstrappedAndCriteria.setBinaryCriteriaSuperClass(bootstrappedBinaryCriteria);
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        var bootstrappedBinaryCriteria = this.handleBinaryCriteria(orCriteria);

        var bootstrappedAndCriteria = new klass.model.meta.domain.OrCriteria();
        bootstrappedAndCriteria.setBinaryCriteriaSuperClass(bootstrappedBinaryCriteria);
    }

    @Nonnull
    private klass.model.meta.domain.BinaryCriteria handleBinaryCriteria(@Nonnull BinaryCriteria binaryCriteria)
    {
        var bootstrappedLeft = BootstrapCriteriaVisitor.convert(
                this.bootstrappedParametersByParameter,
                binaryCriteria.getLeft());
        var bootstrappedRight = BootstrapCriteriaVisitor.convert(
                this.bootstrappedParametersByParameter,
                binaryCriteria.getRight());

        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        bootstrappedCriteria.insert();
        this.bootstrappedCriteria = bootstrappedCriteria;

        var bootstrappedBinaryCriteria = new klass.model.meta.domain.BinaryCriteria();
        bootstrappedBinaryCriteria.setCriteriaSuperClass(bootstrappedCriteria);
        bootstrappedBinaryCriteria.setLeft(bootstrappedLeft);
        bootstrappedBinaryCriteria.setRight(bootstrappedRight);
        return bootstrappedBinaryCriteria;
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
    {
        ExpressionValue sourceValue = operatorCriteria.getSourceValue();
        ExpressionValue targetValue = operatorCriteria.getTargetValue();

        var bootstrappedSourceValue =
                BootstrapExpressionValueVisitor.convert(this.bootstrappedParametersByParameter, sourceValue);
        var bootstrappedTargetValue =
                BootstrapExpressionValueVisitor.convert(this.bootstrappedParametersByParameter, targetValue);

        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        bootstrappedCriteria.insert();

        var bootstrappedOperatorCriteria = new klass.model.meta.domain.OperatorCriteria();
        bootstrappedOperatorCriteria.setCriteriaSuperClass(bootstrappedCriteria);
        bootstrappedOperatorCriteria.setOperator(operatorCriteria.getOperator().getOperatorText());
        long sourceValueId = bootstrappedSourceValue.getId();
        long targetValueId = bootstrappedTargetValue.getId();
        bootstrappedOperatorCriteria.setSourceExpressionId(sourceValueId);
        bootstrappedOperatorCriteria.setSourceExpressionId(targetValueId);

        this.bootstrappedCriteria = bootstrappedCriteria;
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        var bootstrappedMemberReferencePath = new klass.model.meta.domain.MemberReferencePath();
        bootstrappedMemberReferencePath.setExpressionValueSuperClass(bootstrappedExpressionValue);

        MemberReferencePath memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        Klass               klass                 = memberExpressionValue.getKlass();
        DataTypeProperty    property              = memberExpressionValue.getProperty();

        bootstrappedMemberReferencePath.setClassName(klass.getName());
        bootstrappedMemberReferencePath.setPropertyClassName(property.getOwningClassifier().getName());
        bootstrappedMemberReferencePath.setPropertyName(property.getName());
        bootstrappedMemberReferencePath.insert();

        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        bootstrappedCriteria.insert();

        var bootstrappedEdgePointCriteria = new klass.model.meta.domain.EdgePointCriteria();
        bootstrappedEdgePointCriteria.setCriteriaSuperClass(bootstrappedCriteria);
        bootstrappedEdgePointCriteria.setMemberReferencePath(bootstrappedMemberReferencePath);

        this.bootstrappedCriteria = bootstrappedCriteria;
    }
}
