package cool.klass.reladomo.sample.data;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class KlassOptionalDataGenerator extends AbstractKlassDataGenerator
{
    private final OptionalDataTypePropertyVisitor visitor = new OptionalDataTypePropertyVisitor();

    protected KlassOptionalDataGenerator(@Nonnull DataStore dataStore)
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
        if (!dataTypeProperty.isRequired())
        {
            return;
        }

        this.generate(persistentInstance, dataTypeProperty);
    }
}
