package cool.klass.model.meta.domain.api.projection;

import org.eclipse.collections.api.list.ImmutableList;

public interface ProjectionParent extends ProjectionElement
{
    @Override
    ImmutableList<ProjectionElement> getChildren();
}
