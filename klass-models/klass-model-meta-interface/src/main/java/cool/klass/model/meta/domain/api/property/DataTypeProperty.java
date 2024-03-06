package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;

public interface DataTypeProperty extends Property
{
    @Nonnull
    @Override
    DataType getType();

    @Nonnull
    ImmutableList<PropertyModifier> getPropertyModifiers();

    ImmutableListMultimap<AssociationEnd, DataTypeProperty> getKeysMatchingThisForeignKey();

    ImmutableListMultimap<AssociationEnd, DataTypeProperty> getForeignKeysMatchingThisKey();

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

    boolean isForeignKey();
}
