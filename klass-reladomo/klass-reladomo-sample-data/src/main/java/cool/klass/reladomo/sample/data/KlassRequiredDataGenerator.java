package cool.klass.reladomo.sample.data;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class KlassRequiredDataGenerator extends AbstractKlassDataGenerator
{
    private final RequiredDataTypePropertyVisitor visitor = new RequiredDataTypePropertyVisitor();

    public KlassRequiredDataGenerator(DataStore dataStore)
    {
        super(dataStore);
    }

    @Override
    protected Object getNonNullValue(DataTypeProperty dataTypeProperty)
    {
        dataTypeProperty.visit(this.visitor);
        return this.visitor.getResult();
    }

    @Override
    protected void generateIfRequired(Object persistentInstance, DataTypeProperty dataTypeProperty)
    {
        this.generate(persistentInstance, dataTypeProperty);
    }
}
