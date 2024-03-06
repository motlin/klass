package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

public interface ProjectionProjectionReference extends ProjectionWithAssociationEnd
{
    @Override
    default void enter(@Nonnull ProjectionListener listener)
    {
        listener.enterProjectionProjectionReference(this);
    }

    @Override
    default void exit(@Nonnull ProjectionListener listener)
    {
        listener.exitProjectionProjectionReference(this);
    }
}
