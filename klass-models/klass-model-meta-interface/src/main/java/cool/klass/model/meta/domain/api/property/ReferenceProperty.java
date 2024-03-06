package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.order.OrderBy;
import org.eclipse.collections.api.list.ImmutableList;

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

    @Nonnull
    ImmutableList<Modifier> getModifiers();

    // TODO: Delete overrides
    default boolean isOwned()
    {
        return this.getModifiers().anySatisfy(modifier -> modifier.is("owned"));
    }

    default boolean isVersion()
    {
        return this.getModifiers().anySatisfy(Modifier::isVersion);
    }

    default boolean isAudit()
    {
        return this.isCreatedBy() || this.isLastUpdatedBy();
    }

    default boolean isCreatedBy()
    {
        return this.getModifiers().anySatisfy(Modifier::isCreatedBy);
    }

    default boolean isLastUpdatedBy()
    {
        return this.getModifiers().anySatisfy(Modifier::isLastUpdatedBy);
    }

    default boolean isFinal()
    {
        return this.getModifiers().anySatisfy(modifier -> modifier.is("final"));
    }

    default boolean isPrivate()
    {
        return this.getModifiers().anySatisfy(modifier -> modifier.is("private"));
    }
}
