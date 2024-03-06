package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.eclipse.collections.api.list.ImmutableList;

@JsonIgnoreProperties("@type")
public class ProjectionDTO
        extends PackageableElementDTO
{
    private final ReferenceDTO klass;

    private final ImmutableList<ProjectionChildDTO> children;

    public ProjectionDTO(
            String name,
            String packageName,
            ReferenceDTO klass,
            ImmutableList<ProjectionChildDTO> children)
    {
        super(name, packageName);
        this.klass    = klass;
        this.children = children;
    }

    public ReferenceDTO getKlass()
    {
        return this.klass;
    }

    public ImmutableList<ProjectionChildDTO> getChildren()
    {
        return this.children;
    }
}