package cool.klass.reladomo.test.data;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;

public abstract class ReladomoKlassDataGenerator
{
    protected final DataStore dataStore;
    protected final Klass     klass;

    protected ReladomoKlassDataGenerator(DataStore dataStore, Klass klass)
    {
        this.dataStore = dataStore;
        this.klass = klass;
    }

    protected abstract Object getNonNullValue(DataTypeProperty dataTypeProperty);

    public void generateIfRequired()
    {
        ImmutableList<DataTypeProperty> keyProperties = this.klass.getKeyProperties().reject(DataTypeProperty::isID);

        ImmutableList<Object> keyValues = keyProperties.collect(this::getNonNullValue);

        Object persistentInstance = this.instantiate(keyValues);

        this.klass.getDataTypeProperties()
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isSystem)
                .each(dataTypeProperty -> this.generateIfRequired(persistentInstance, dataTypeProperty));

        this.dataStore.insert(persistentInstance);
    }

    private Object instantiate(ImmutableList<Object> keyValues)
    {
        if (this.klass.isValidTemporal())
        {
            throw new AssertionError();
        }
        return this.dataStore.instantiate(this.klass, keyValues);
    }

    protected abstract void generateIfRequired(Object persistentInstance, DataTypeProperty dataTypeProperty);

    protected final void generate(Object persistentInstance, DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isVersion())
        {
            this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, 1);
            return;
        }

        if (dataTypeProperty.isForeignKey())
        {
            DataTypeProperty keyProperty = this.getMatchingKeyProperty(dataTypeProperty);
            Object           value       = this.getNonNullValue(keyProperty);
            this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, value);
        }
        else
        {
            Object value = this.getNonNullValue(dataTypeProperty);
            this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, value);
        }
    }

    private DataTypeProperty getMatchingKeyProperty(DataTypeProperty dataTypeProperty)
    {
        ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = dataTypeProperty.getKeysMatchingThisForeignKey();
        if (keysMatchingThisForeignKey.size() > 1)
        {
            // TODO: Throw or do something better in this case
            return dataTypeProperty;
        }
        return keysMatchingThisForeignKey.valuesView().getOnly();
    }
}
