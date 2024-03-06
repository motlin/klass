package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SuperInterfaceMappingDTO
{
    private final ReferenceDTO superInterface;

    @JsonCreator
    public SuperInterfaceMappingDTO(ReferenceDTO superInterface)
    {
        this.superInterface = superInterface;
    }

    public ReferenceDTO getSuperInterface()
    {
        return this.superInterface;
    }
}
