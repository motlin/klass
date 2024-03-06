package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.Klass;

public interface AssociationEnd extends ReferenceProperty
{
    @Nonnull
    @Override
    Klass getType();

    @Nonnull
    @Override
    Klass getOwningClassifier();

    @Override
    default void visit(@Nonnull PropertyVisitor visitor)
    {
        visitor.visitAssociationEnd(this);
    }

    @Nonnull
    default AssociationEnd getOpposite()
    {
        Association association = this.getOwningAssociation();

        if (this == association.getSourceAssociationEnd())
        {
            return association.getTargetAssociationEnd();
        }

        if (this == association.getTargetAssociationEnd())
        {
            return association.getSourceAssociationEnd();
        }

        throw new AssertionError();
    }

    @Nonnull
    Association getOwningAssociation();
}
