package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class ProjectionReferencePropertyDTO
        extends ProjectionChildDTO
{
    private final ImmutableList<ProjectionChildDTO> children;

    public ProjectionReferencePropertyDTO(
            String name,
            ImmutableList<ProjectionChildDTO> children)
    {
        super(name);
        this.children = children;
    }

    public ImmutableList<ProjectionChildDTO> getChildren()
    {
        return this.children;
    }
}
