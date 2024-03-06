package cool.klass.model.meta.domain.json.dto;

public abstract class NamedElementDTO
{
    private final String name;

    protected NamedElementDTO(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}
