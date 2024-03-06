package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

public interface ProjectionAssociationEnd extends ProjectionWithAssociationEnd
{
    @Override
    default void visit(ProjectionVisitor visitor)
    {
        visitor.visitProjectionAssociationEnd(this);
    }

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
}
