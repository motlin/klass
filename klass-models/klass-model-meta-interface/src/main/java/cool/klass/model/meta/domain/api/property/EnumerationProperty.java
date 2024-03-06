package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Enumeration;

public interface EnumerationProperty extends DataTypeProperty
{
    @Override
    default void visit(PropertyVisitor visitor)
    {
        visitor.visitEnumerationProperty(this);
    }

    @Nonnull
    @Override
    Enumeration getType();

    @Override
    default boolean isTemporalRange()
    {
        return false;
    }

    @Override
    default boolean isTemporalInstant()
    {
        return false;
    }

    @Override
    default boolean isTemporal()
    {
        return false;
    }
}
