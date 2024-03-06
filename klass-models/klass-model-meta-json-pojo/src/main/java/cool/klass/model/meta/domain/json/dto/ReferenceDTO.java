package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ReferenceDTO
        extends NamedElementDTO
{
    @JsonCreator
    public ReferenceDTO(String name)
    {
        super(name);
    }
}
