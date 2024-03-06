package cool.klass.model.meta.domain.api.projection;

import org.eclipse.collections.api.list.ImmutableList;

public interface ProjectionElement
{
    ImmutableList<? extends ProjectionElement> getChildren();

    void enter(ProjectionListener listener);

    void exit(ProjectionListener listener);
}
