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
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;
import org.eclipse.collections.api.list.ImmutableList;

public class BootstrapCriteriaVisitor implements CriteriaVisitor
{
    private klass.model.meta.domain.Criteria bootstrappedCriteria;

    public static klass.model.meta.domain.Criteria convert(Criteria criteria)
    {
        BootstrapCriteriaVisitor visitor = new BootstrapCriteriaVisitor();
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
                BootstrapExpressionValueVisitor.convert(sourceValue);
        klass.model.meta.domain.ExpressionValue bootstrappedTargetValue =
                BootstrapExpressionValueVisitor.convert(targetValue);

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

        MemberReferencePath           memberExpressionValue = edgePointCriteria.getMemberExpressionValue();
        Klass                         klass                 = memberExpressionValue.getKlass();
        ImmutableList<AssociationEnd> associationEnds       = memberExpressionValue.getAssociationEnds();
        DataTypeProperty              property              = memberExpressionValue.getProperty();

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
            klass.model.meta.domain.BinaryCriteria bootstrappedCriteria,
            BinaryCriteria binaryCriteria)
    {
        klass.model.meta.domain.Criteria bootstrappedLeft  = BootstrapCriteriaVisitor.convert(binaryCriteria.getLeft());
        klass.model.meta.domain.Criteria bootstrappedRight = BootstrapCriteriaVisitor.convert(binaryCriteria.getRight());

        bootstrappedCriteria.setLeft(bootstrappedLeft);
        bootstrappedCriteria.setRight(bootstrappedRight);

        this.handleCriteria(bootstrappedCriteria, binaryCriteria);
    }

    private void handleCriteria(
            klass.model.meta.domain.Criteria bootstrappedCriteria,
            Criteria criteria)
    {
        KlassBootstrapWriter.handleElement(bootstrappedCriteria, criteria);
        bootstrappedCriteria.insert();
        this.bootstrappedCriteria = bootstrappedCriteria;
    }
}
