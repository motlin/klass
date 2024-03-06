package cool.klass.model.meta.domain.json.dto;

import java.util.Optional;

public class EnumerationLiteralDTO
        extends NamedElementDTO
{
    private final Optional<String> prettyName;

    public EnumerationLiteralDTO(String name, Optional<String> prettyName)
    {
        super(name);
        this.prettyName = prettyName;
    }

    public Optional<String> getPrettyName()
    {
        return this.prettyName;
    }
}
