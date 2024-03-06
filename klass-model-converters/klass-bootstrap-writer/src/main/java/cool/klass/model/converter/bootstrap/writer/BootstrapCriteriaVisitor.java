package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.BinaryCriteria;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;

public class BootstrapCriteriaVisitor implements CriteriaVisitor
{
    private final Criteria                         criteria;
    private       klass.model.meta.domain.Criteria bootstrappedCriteria;

    public BootstrapCriteriaVisitor(Criteria criteria)
    {
        this.criteria = criteria;
    }

    public static klass.model.meta.domain.Criteria convert(Criteria criteria)
    {
        BootstrapCriteriaVisitor visitor = new BootstrapCriteriaVisitor(criteria);
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
        // TODO
        this.handleCriteria(new klass.model.meta.domain.AllCriteria(), operatorCriteria);
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        // TODO
        this.handleCriteria(new klass.model.meta.domain.AllCriteria(), edgePointCriteria);
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
