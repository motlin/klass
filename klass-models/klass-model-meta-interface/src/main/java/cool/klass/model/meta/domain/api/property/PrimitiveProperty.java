package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PrimitiveType;

public interface PrimitiveProperty extends DataTypeProperty
{
    @Override
    default boolean isTemporalRange()
    {
        return this.getType().isTemporalRange();
    }

    @Override
    default boolean isTemporalInstant()
    {
        return this.getType().isTemporalInstant();
    }

    @Override
    default boolean isTemporal()
    {
        return this.getType().isTemporal();
    }

    boolean isID();

    @Override
    @Nonnull
    PrimitiveType getType();
}
