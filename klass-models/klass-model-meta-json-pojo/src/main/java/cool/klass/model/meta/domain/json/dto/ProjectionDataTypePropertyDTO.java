package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class ProjectionDataTypePropertyDTO
        extends ProjectionChildDTO
{
    public ProjectionDataTypePropertyDTO(String name, ImmutableList<Object> children)
    {
        super(name);
        if (children.notEmpty())
        {
            throw new IllegalArgumentException(children.makeString());
        }
    }
}
