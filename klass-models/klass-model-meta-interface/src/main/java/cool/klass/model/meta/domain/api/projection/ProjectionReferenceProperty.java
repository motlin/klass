package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

public interface ProjectionReferenceProperty
        extends ProjectionWithReferenceProperty
{
    @Override
    default void visit(@Nonnull ProjectionVisitor visitor)
    {
        visitor.visitProjectionReferenceProperty(this);
    }

    @Override
    default void enter(@Nonnull ProjectionListener listener)
    {
        listener.enterProjectionReferenceProperty(this);
    }

    @Override
    default void exit(@Nonnull ProjectionListener listener)
    {
        listener.exitProjectionReferenceProperty(this);
    }
}
