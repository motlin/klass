package cool.klass.model.meta.domain.api.property;

import cool.klass.model.meta.domain.api.IEnumeration;

public interface IEnumerationProperty extends IDataTypeProperty<IEnumeration>
{
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
