package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.TopLevelElementVisitor;

public interface Projection
        extends ProjectionParent, TopLevelElement
{
    @Override
    default void visit(TopLevelElementVisitor visitor)
    {
        visitor.visitProjection(this);
    }

    @Override
    default void visit(@Nonnull ProjectionVisitor visitor)
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
