package cool.klass.data.store;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import org.eclipse.collections.api.map.MapIterable;

public interface DataStore
{
    <Result> Result runInTransaction(TransactionalCommand<Result> transactionalCommand);

    void runInTransaction(Runnable runnable);

    List<Object> findAll(Klass klass);

    Object findByKey(Klass klass, MapIterable<DataTypeProperty, Object> keys);

    List<Object> findByKeyReturningList(Klass klass, MapIterable<DataTypeProperty, Object> keys);

    default Object getReferenceProperty(Object persistentInstance, ReferenceProperty referenceProperty)
    {
        if (referenceProperty.getMultiplicity().isToOne())
        {
            return this.getToOne(persistentInstance, referenceProperty);
        }
        if (referenceProperty.getMultiplicity().isToMany())
        {
            return this.getToMany(persistentInstance, referenceProperty);
        }
        throw new IllegalStateException("Unknown multiplicity: " + referenceProperty.getMultiplicity());
    }

    Object getToOne(Object persistentSourceInstance, @Nonnull ReferenceProperty referenceProperty);

    @Nonnull
    List<Object> getToMany(Object persistentSourceInstance, ReferenceProperty referenceProperty);

    @Nullable
    Object getDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty);

    boolean setDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty, Object newValue);

    @Nonnull
    Object instantiate(Klass klass, MapIterable<DataTypeProperty, Object> keys);

    void insert(Object persistentInstance);

    boolean setToOne(Object persistentSourceInstance, AssociationEnd associationEnd, Object persistentTargetInstance);

    void deleteOrTerminate(@Nonnull Object persistentInstance);

    void purgeAll(@Nonnull Klass klass);

    boolean isInstanceOf(Object persistentInstance, Classifier classifier);

    Klass getMostSpecificSubclass(Object persistentInstance, Klass klass);

    Object getSuperClass(Object persistentInstance, Klass klass);
}
