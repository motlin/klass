package cool.klass.reladomo.sample.data;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractKlassDataGenerator
{
    protected final DataStore dataStore;

    protected AbstractKlassDataGenerator(DataStore dataStore)
    {
        this.dataStore = dataStore;
    }

    protected abstract Object getNonNullValue(DataTypeProperty dataTypeProperty);

    public void generateIfRequired(@Nonnull Klass klass)
    {
        Object persistentInstance = this.instantiate(klass);

        klass.getDataTypeProperties()
                .reject(DataTypeProperty::isKey)
                .reject(DataTypeProperty::isSystem)
                .each(dataTypeProperty -> this.generateIfRequired(persistentInstance, dataTypeProperty));

        this.dataStore.insert(persistentInstance);
    }

    @Nonnull
    private Object instantiate(@Nonnull Klass klass)
    {
        ImmutableList<DataTypeProperty> keyProperties = klass.getKeyProperties().reject(DataTypeProperty::isID);
        ImmutableList<Object>           keyValues     = keyProperties.collect(this::getNonNullValue);

        if (klass.isValidTemporal())
        {
            throw new AssertionError();
        }
        return this.dataStore.instantiate(klass, keyValues);
    }

    protected abstract void generateIfRequired(Object persistentInstance, DataTypeProperty dataTypeProperty);

    protected final void generate(Object persistentInstance, @Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isVersion())
        {
            this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, 1);
            return;
        }

        Object value = this.getNonNullValue(dataTypeProperty);
        this.dataStore.setDataTypeProperty(persistentInstance, dataTypeProperty, value);
    }
}
