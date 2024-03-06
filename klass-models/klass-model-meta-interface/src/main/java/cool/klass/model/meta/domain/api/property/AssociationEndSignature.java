package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.modifier.AssociationEndModifier;
import org.eclipse.collections.api.list.ImmutableList;

public interface AssociationEndSignature
        extends ReferenceProperty
{
    @Override
    default void visit(@Nonnull PropertyVisitor visitor)
    {
        visitor.visitAssociationEndSignature(this);
    }

    // TODO: Pull these up to ReferenceProperty
    @Nonnull
    ImmutableList<AssociationEndModifier> getAssociationEndModifiers();

    // TODO: Delete overrides
    default boolean isOwned()
    {
        return this.getAssociationEndModifiers().anySatisfy(AssociationEndModifier::isOwned);
    }

    default boolean isVersion()
    {
        return this.getAssociationEndModifiers().anySatisfy(AssociationEndModifier::isVersion);
    }

    default boolean isFinal()
    {
        return this.getAssociationEndModifiers().anySatisfy(AssociationEndModifier::isFinal);
    }
}
