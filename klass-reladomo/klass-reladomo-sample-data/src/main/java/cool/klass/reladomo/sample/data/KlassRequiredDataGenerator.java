package cool.klass.reladomo.sample.data;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class KlassRequiredDataGenerator extends AbstractKlassDataGenerator
{
    public KlassRequiredDataGenerator(DataStore dataStore, Klass klass)
    {
        super(dataStore, klass);
    }

    @Override
    protected Object getNonNullValue(DataTypeProperty dataTypeProperty)
    {
        RequiredDataTypePropertyVisitor visitor = new RequiredDataTypePropertyVisitor();
        dataTypeProperty.visit(visitor);
        return visitor.getResult();
    }

    @Override
    protected void generateIfRequired(Object persistentInstance, DataTypeProperty dataTypeProperty)
    {
        this.generate(persistentInstance, dataTypeProperty);
    }
}
