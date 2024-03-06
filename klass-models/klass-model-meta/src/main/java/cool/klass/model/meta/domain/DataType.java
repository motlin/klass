package cool.klass.model.meta.domain;

/**
 * A DataType is a kind of Classifier. DataType differs from Class in that instances of a DataType are identified only by their value. All instances of a DataType with the same value are considered to be equal instances.
 */
public interface DataType extends Type
{
    interface DataTypeGetter extends TypeGetter
    {
        DataType getType();
    }
}
