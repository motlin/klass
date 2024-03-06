package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.NamedElement;
import org.eclipse.collections.api.list.ImmutableList;

public interface ProjectionElement extends NamedElement
{
    @Nonnull
    @Override
    String getName();

    ImmutableList<? extends ProjectionElement> getChildren();

    void enter(ProjectionListener listener);

    void exit(ProjectionListener listener);

    default void visit(ProjectionListener listener)
    {
        try
        {
            this.enter(listener);
        }
        finally
        {
            this.exit(listener);
        }
    }
}
