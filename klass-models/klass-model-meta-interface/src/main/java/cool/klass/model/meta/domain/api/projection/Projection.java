package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PackageableElement;

public interface Projection extends PackageableElement, ProjectionParent
{
    @Override
    default void visit(ProjectionVisitor visitor)
    {
        visitor.visitProjection(this);
    }

    @Override
    default void enter(@Nonnull ProjectionListener listener)
    {
        listener.enterProjection(this);
    }

    @Override
    default void exit(@Nonnull ProjectionListener listener)
    {
        listener.exitProjection(this);
    }
}
