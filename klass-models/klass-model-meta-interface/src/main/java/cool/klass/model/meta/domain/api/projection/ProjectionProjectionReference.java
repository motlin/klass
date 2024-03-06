package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;

public interface ProjectionProjectionReference extends ProjectionWithAssociationEnd
{
    Projection getProjection();

    @Nonnull
    @Override
    default Klass getKlass()
    {
        return this.getProjection().getKlass();
    }

    @Override
    default ImmutableList<? extends ProjectionChild> getChildren()
    {
        return this.getProjection().getChildren();
    }

    @Override
    default void visit(@Nonnull ProjectionVisitor visitor)
    {
        visitor.visitProjectionProjectionReference(this);
    }

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
