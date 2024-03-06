package cool.klass.data.store;

import java.util.List;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface DataStore
{
    void runInTransaction(TransactionalCommand transactionalCommand);

    void runInTransaction(Runnable runnable);

    Object findByKey(Klass klass, ImmutableList<Object> keys);

    Object getToOne(Object persistentSourceInstance, AssociationEnd associationEnd);

    List<Object> getToMany(Object persistentSourceInstance, AssociationEnd associationEnd);

    Object getDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty);

    void setDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty, Object newValue);

    Object instantiate(Klass klass, ImmutableList<Object> keys);

    void insert(Object persistentInstance);

    void setToOne(Object persistentSourceInstance, AssociationEnd associationEnd, Object persistentTargetInstance);

    void deleteOrTerminate(Object persistentInstance);

    boolean isInstanceOf(Object persistentInstance, Classifier classifier);
}
