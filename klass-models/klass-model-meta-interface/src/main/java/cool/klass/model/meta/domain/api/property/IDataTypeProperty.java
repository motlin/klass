package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public interface IDataTypeProperty<T extends DataType> extends IProperty<T>
{
    @Nonnull
    ImmutableList<IPropertyModifier> getPropertyModifiers();

    boolean isKey();

    boolean isOptional();

    boolean isTemporalRange();

    boolean isTemporalInstant();

    boolean isTemporal();
}
