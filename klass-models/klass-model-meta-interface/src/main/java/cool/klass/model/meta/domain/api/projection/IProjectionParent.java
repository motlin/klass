package cool.klass.model.meta.domain.api.projection;

import cool.klass.model.meta.domain.api.INamedElement;
import org.eclipse.collections.api.list.ImmutableList;

public interface IProjectionParent extends INamedElement, ProjectionElement
{
    @Override
    ImmutableList<ProjectionElement> getChildren();
}
