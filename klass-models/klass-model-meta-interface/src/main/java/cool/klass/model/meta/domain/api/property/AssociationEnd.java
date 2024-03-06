package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.OrderBy;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: Super class for reference-type-property?
public interface AssociationEnd extends Property
{
    @Nonnull
    Multiplicity getMultiplicity();

    @Nonnull
    ImmutableList<AssociationEndModifier> getAssociationEndModifiers();

    boolean isOwned();

    AssociationEnd getOpposite();

    @Nonnull
    Association getOwningAssociation();

    @Nonnull
    Optional<OrderBy> getOrderBy();

    @Override
    @Nonnull
    Klass getType();
}
