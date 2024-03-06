package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.OrderBy;

// TODO: Super class for reference-type-property?
public interface ParameterizedProperty extends Property
{
    @Nonnull
    Multiplicity getMultiplicity();

    @Nonnull
    Optional<OrderBy> getOrderBy();

    @Override
    @Nonnull
    Klass getType();
}
