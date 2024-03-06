package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.visitor.DataTypePropertyVisitor;

public interface EnumerationProperty extends DataTypeProperty
{
    @Override
    default void visit(@Nonnull PropertyVisitor visitor)
    {
        visitor.visitEnumerationProperty(this);
    }

    @Override
    default void visit(@Nonnull DataTypePropertyVisitor visitor)
    {
        visitor.visitEnumerationProperty(this);
    }

    @Nonnull
    @Override
    Enumeration getType();

    @Override
    default boolean isID()
    {
        return false;
    }

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

    @Override
    default boolean isVersion()
    {
        return false;
    }
}
