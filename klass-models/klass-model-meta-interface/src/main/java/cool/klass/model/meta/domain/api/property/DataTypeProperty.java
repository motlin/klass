package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public interface DataTypeProperty extends Property
{
    @Nonnull
    @Override
    DataType getType();

    @Nonnull
    ImmutableList<PropertyModifier> getPropertyModifiers();

    boolean isKey();

    boolean isOptional();

    boolean isTemporalRange();

    boolean isTemporalInstant();

    boolean isTemporal();
}
