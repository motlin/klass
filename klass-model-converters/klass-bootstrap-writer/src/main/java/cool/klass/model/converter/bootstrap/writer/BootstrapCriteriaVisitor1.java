package cool.klass.model.converter.bootstrap.writer;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import klass.model.meta.domain.CriteriaList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class BootstrapCriteriaVisitor1
        implements CriteriaVisitor
{
    private final MutableMap<cool.klass.model.meta.domain.api.criteria.Criteria, klass.model.meta.domain.Criteria> criteriaByCriteria = MapAdapter.adapt(new LinkedHashMap<>());

    private final CriteriaList bootstrappedCriteria = new CriteriaList();

    public ImmutableMap<Criteria, klass.model.meta.domain.Criteria> getCriteriaByCriteria()
    {
        return this.criteriaByCriteria.toImmutable();
    }

    public CriteriaList getBootstrappedCriteria()
    {
        return this.bootstrappedCriteria;
    }

    @Override
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        this.criteriaByCriteria.put(allCriteria, bootstrappedCriteria);
        this.bootstrappedCriteria.add(bootstrappedCriteria);
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
    {
        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        this.criteriaByCriteria.put(operatorCriteria, bootstrappedCriteria);
        this.bootstrappedCriteria.add(bootstrappedCriteria);
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        this.criteriaByCriteria.put(edgePointCriteria, bootstrappedCriteria);
        this.bootstrappedCriteria.add(bootstrappedCriteria);
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        this.criteriaByCriteria.put(andCriteria, bootstrappedCriteria);
        this.bootstrappedCriteria.add(bootstrappedCriteria);

        andCriteria.getLeft().visit(this);
        andCriteria.getRight().visit(this);
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        var bootstrappedCriteria = new klass.model.meta.domain.Criteria();
        this.criteriaByCriteria.put(orCriteria, bootstrappedCriteria);
        this.bootstrappedCriteria.add(bootstrappedCriteria);

        orCriteria.getLeft().visit(this);
        orCriteria.getRight().visit(this);
    }
}
