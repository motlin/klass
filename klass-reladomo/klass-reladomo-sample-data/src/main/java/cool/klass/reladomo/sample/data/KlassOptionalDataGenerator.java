package cool.klass.reladomo.sample.data;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class KlassOptionalDataGenerator extends AbstractKlassDataGenerator
{
    protected KlassOptionalDataGenerator(DataStore dataStore, Klass klass)
    {
        super(dataStore, klass);
    }

    @Override
    protected Object getNonNullValue(DataTypeProperty dataTypeProperty)
    {
        OptionalDataTypePropertyVisitor visitor = new OptionalDataTypePropertyVisitor();
        dataTypeProperty.visit(visitor);
        return visitor.getResult();
    }

    @Override
    protected void generateIfRequired(Object persistentInstance, DataTypeProperty dataTypeProperty)
    {
        if (!dataTypeProperty.isRequired())
        {
            return;
        }

        this.generate(persistentInstance, dataTypeProperty);
    }
}
