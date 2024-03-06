package cool.klass.reladomo.test.data;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class ReladomoKlassOptionalDataGenerator extends ReladomoKlassDataGenerator
{
    protected ReladomoKlassOptionalDataGenerator(DataStore dataStore, Klass klass)
    {
        super(dataStore, klass);
    }

    @Override
    protected Object getNonNullValue(DataTypeProperty dataTypeProperty)
    {
        ReladomoOptionalDataTypePropertyVisitor visitor = new ReladomoOptionalDataTypePropertyVisitor();
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
