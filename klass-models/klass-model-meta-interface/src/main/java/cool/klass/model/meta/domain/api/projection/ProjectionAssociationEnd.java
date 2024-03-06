package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.IAssociationEnd;

public interface ProjectionAssociationEnd extends IProjectionParent
{
    @Override
    default void enter(ProjectionListener listener)
    {
        listener.enterProjectionAssociationEnd(this);
    }

    @Override
    default void exit(ProjectionListener listener)
    {
        listener.exitProjectionAssociationEnd(this);
    }

    @Nonnull
    IAssociationEnd getAssociationEnd();
}
