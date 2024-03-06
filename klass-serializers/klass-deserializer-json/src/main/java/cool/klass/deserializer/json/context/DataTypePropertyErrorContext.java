package cool.klass.deserializer.json.context;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class DataTypePropertyErrorContext implements ErrorContext
{
    private final DataTypeProperty dataTypeProperty;

    public DataTypePropertyErrorContext(DataTypeProperty dataTypeProperty)
    {
        this.dataTypeProperty = Objects.requireNonNull(dataTypeProperty);
    }

    @Nonnull
    @Override
    public String toString()
    {
        return this.dataTypeProperty.getName();
    }
}
