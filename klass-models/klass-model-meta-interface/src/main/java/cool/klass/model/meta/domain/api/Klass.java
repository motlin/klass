package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface Klass extends Type, PackageableElement
{
    // TODO: Replace with an implementation that preserves order
    default ImmutableList<Property> getProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDataTypeProperties())
                .newWithAll(this.getAssociationEnds());
    }

    ImmutableList<DataTypeProperty> getDataTypeProperties();

    ImmutableList<AssociationEnd> getAssociationEnds();

    // TODO: Override for efficiency?
    @Nonnull
    default Optional<Property> getPropertyByName(String name)
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = this.getDataTypeProperties().select(each -> each.getName().equals(name));
        ImmutableList<AssociationEnd>   associationEnds             = this.getAssociationEnds().select(each -> each.getName().equals(name));

        if (dataTypeProperties.isEmpty() && associationEnds.isEmpty())
        {
            return Optional.empty();
        }

        if (dataTypeProperties.size() + associationEnds.size() > 1)
        {
            throw new AssertionError(name);
        }

        if (dataTypeProperties.notEmpty())
        {
            return Optional.of(dataTypeProperties.getOnly());
        }

        return Optional.of(associationEnds.getOnly());
    }

    default ImmutableList<DataTypeProperty> getKeyProperties()
    {
        return this.getDataTypeProperties().select(DataTypeProperty::isKey);
    }

    @Nonnull
    Optional<AssociationEnd> getVersionProperty();

    @Nonnull
    Optional<AssociationEnd> getVersionedProperty();

    @Nonnull
    ImmutableList<ClassModifier> getClassModifiers();

    boolean isUser();

    boolean isTransient();
}
