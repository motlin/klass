package cool.klass.model.meta.domain.json.dto;

public class SuperInterfaceMappingDTO
{
    private final ReferenceDTO superInterface;

    public SuperInterfaceMappingDTO(ReferenceDTO superInterface)
    {
        this.superInterface = superInterface;
    }

    public ReferenceDTO getSuperInterface()
    {
        return this.superInterface;
    }
}
