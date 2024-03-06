package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IAssociation;
import cool.klass.model.meta.domain.api.IKlass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.IOrderBy;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: Super class for reference-type-property?
public interface IAssociationEnd extends IProperty<IKlass>
{
    @Nonnull
    Multiplicity getMultiplicity();

    @Nonnull
    ImmutableList<IAssociationEndModifier> getAssociationEndModifiers();

    boolean isOwned();

    IAssociationEnd getOpposite();

    @Nonnull
    IAssociation getOwningAssociation();

    @Nonnull
    Optional<IOrderBy> getOrderBy();
}
