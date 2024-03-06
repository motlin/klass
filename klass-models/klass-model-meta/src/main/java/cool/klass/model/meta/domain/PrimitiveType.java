package cool.klass.model.meta.domain;

/**
 * Predefined native types.
 */
public abstract class PrimitiveType extends DataType
{
    protected PrimitiveType(String name)
    {
        super(name, "klass.meta");
    }
}
