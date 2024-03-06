package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.OrderBy;

public interface ReferenceProperty
        extends Property
{
    @Nonnull
    Multiplicity getMultiplicity();

    @Override
    default boolean isRequired()
    {
        return this.getMultiplicity().isRequired();
    }

    @Override
    default boolean isDerived()
    {
        // TODO: derived ReferenceProperties
        return false;
    }

    @Nonnull
    Optional<OrderBy> getOrderBy();

    @Override
    @Nonnull
    Classifier getType();
}
