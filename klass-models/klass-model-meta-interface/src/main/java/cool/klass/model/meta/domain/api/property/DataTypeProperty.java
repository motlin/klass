package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import org.eclipse.collections.api.list.ImmutableList;

public interface DataTypeProperty extends Property
{
    @Nonnull
    @Override
    DataType getType();

    @Nonnull
    ImmutableList<PropertyModifier> getPropertyModifiers();

    default boolean isKey()
    {
        return this.getPropertyModifiers().anySatisfy(PropertyModifier::isKey);
    }

    boolean isID();

    default boolean isAudit()
    {
        return this.getPropertyModifiers().anySatisfy(PropertyModifier::isAudit);
    }

    boolean isOptional();

    boolean isTemporalRange();

    boolean isTemporalInstant();

    boolean isTemporal();
}
