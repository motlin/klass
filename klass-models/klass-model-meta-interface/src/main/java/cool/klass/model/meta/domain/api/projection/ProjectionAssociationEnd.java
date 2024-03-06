package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;

public interface ProjectionAssociationEnd extends ProjectionParent
{
    @Override
    default void enter(@Nonnull ProjectionListener listener)
    {
        listener.enterProjectionAssociationEnd(this);
    }

    @Override
    default void exit(@Nonnull ProjectionListener listener)
    {
        listener.exitProjectionAssociationEnd(this);
    }

    @Nonnull
    AssociationEnd getAssociationEnd();

    @Nonnull
    @Override
    default Klass getKlass()
    {
        return this.getAssociationEnd().getOwningKlass();
    }
}
