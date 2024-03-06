package cool.klass.reladomo.sample.data;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class KlassRequiredDataGenerator extends AbstractKlassDataGenerator
{
    private final RequiredDataTypePropertyVisitor visitor = new RequiredDataTypePropertyVisitor();

    public KlassRequiredDataGenerator(@Nonnull DataStore dataStore)
    {
        super(dataStore);
    }

    @Override
    protected Object getNonNullValue(@Nonnull DataTypeProperty dataTypeProperty)
    {
        dataTypeProperty.visit(this.visitor);
        return this.visitor.getResult();
    }

    @Override
    protected void generateIfRequired(Object persistentInstance, @Nonnull DataTypeProperty dataTypeProperty)
    {
        this.generate(persistentInstance, dataTypeProperty);
    }
}
