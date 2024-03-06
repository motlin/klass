package cool.klass.reladomo.persistent.writer.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class DataTypePropertyErrorContext implements ErrorContext
{
    private final DataTypeProperty dataTypeProperty;

    public DataTypePropertyErrorContext(DataTypeProperty dataTypeProperty)
    {
        this.dataTypeProperty = Objects.requireNonNull(dataTypeProperty);
    }

    @Override
    public String toString()
    {
        return this.dataTypeProperty.getName();
    }
}
