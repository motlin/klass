package cool.klass.data.store;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface DataStore
{
    <Result> Result runInTransaction(TransactionalCommand<Result> transactionalCommand);

    void runInTransaction(Runnable runnable);

    Object findByKey(Klass klass, ImmutableList<Object> keys);

    Object getToOne(Object persistentSourceInstance, @Nonnull ReferenceProperty referenceProperty);

    @Nonnull
    List<Object> getToMany(Object persistentSourceInstance, ReferenceProperty referenceProperty);

    @Nullable
    Object getDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty);

    boolean setDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty, Object newValue);

    @Nonnull
    Object instantiate(Klass klass, ImmutableList<Object> keys);

    void insert(Object persistentInstance);

    boolean setToOne(Object persistentSourceInstance, AssociationEnd associationEnd, Object persistentTargetInstance);

    void deleteOrTerminate(@Nonnull Object persistentInstance);

    void purgeAll(@Nonnull Klass klass);

    boolean isInstanceOf(Object persistentInstance, Classifier classifier);
}
