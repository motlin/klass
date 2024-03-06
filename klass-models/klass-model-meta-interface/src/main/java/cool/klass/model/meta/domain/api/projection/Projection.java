package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;

public interface Projection extends PackageableElement, ProjectionParent
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
    Klass getKlass();
}
