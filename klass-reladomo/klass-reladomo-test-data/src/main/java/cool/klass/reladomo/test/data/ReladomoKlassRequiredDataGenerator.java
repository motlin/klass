package cool.klass.reladomo.test.data;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class ReladomoKlassRequiredDataGenerator extends ReladomoKlassDataGenerator
{
    public ReladomoKlassRequiredDataGenerator(DataStore dataStore, Klass klass)
    {
        super(dataStore, klass);
    }

    @Override
    protected Object getNonNullValue(DataTypeProperty dataTypeProperty)
    {
        ReladomoRequiredDataTypePropertyVisitor visitor = new ReladomoRequiredDataTypePropertyVisitor();
        dataTypeProperty.visit(visitor);
        return visitor.getResult();
    }

    @Override
    protected void generateIfRequired(Object persistentInstance, DataTypeProperty dataTypeProperty)
    {
        this.generate(persistentInstance, dataTypeProperty);
    }
}
