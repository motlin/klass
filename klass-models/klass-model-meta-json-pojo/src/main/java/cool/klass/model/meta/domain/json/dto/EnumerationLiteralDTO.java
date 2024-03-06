package cool.klass.model.meta.domain.json.dto;

public class EnumerationLiteralDTO
        extends NamedElementDTO
{
    private final String prettyName;

    public EnumerationLiteralDTO(String name, String prettyName)
    {
        super(name);
        this.prettyName = prettyName;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }
}
