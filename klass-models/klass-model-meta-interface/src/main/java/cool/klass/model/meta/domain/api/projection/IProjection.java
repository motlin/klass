package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IKlass;

public interface IProjection extends IProjectionParent
{
    @Override
    default void enter(ProjectionListener listener)
    {
        listener.enterProjection(this);
    }

    @Override
    default void exit(ProjectionListener listener)
    {
        listener.exitProjection(this);
    }

    @Nonnull
    IKlass getKlass();
}
