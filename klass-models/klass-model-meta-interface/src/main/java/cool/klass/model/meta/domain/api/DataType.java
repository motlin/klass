package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

/**
 * A DataType is a type whose instances are identified only by their value.
 * They are primitives, enumerations, and struct-like records with no keys.
 * All instances of a DataType with the same value are considered to be equal instances.
 */
public interface DataType extends Type
{
    interface DataTypeGetter extends TypeGetter
    {
        @Nonnull
        @Override
        DataType getType();
    }
}
