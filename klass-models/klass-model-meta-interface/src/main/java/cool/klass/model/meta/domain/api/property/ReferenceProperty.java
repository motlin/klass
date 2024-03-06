package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.OrderBy;

public interface ReferenceProperty extends Property
{
    @Nonnull
    Multiplicity getMultiplicity();

    @Nonnull
    Optional<OrderBy> getOrderBy();

    @Override
    @Nonnull
    Klass getType();
}
