package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class ProjectionProjectionReferenceDTO
        extends ProjectionChildDTO
{
    private final ReferenceDTO projection;

    public ProjectionProjectionReferenceDTO(
            String name,
            ReferenceDTO projection,
            ImmutableList<Object> children)
    {
        super(name);
        this.projection = projection;
        if (children.notEmpty())
        {
            throw new IllegalArgumentException(children.makeString());
        }
    }

    public ReferenceDTO getProjection()
    {
        return this.projection;
    }
}
