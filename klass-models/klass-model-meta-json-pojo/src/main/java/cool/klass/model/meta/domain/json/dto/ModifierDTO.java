package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ModifierDTO
        extends NamedElementDTO
{
    @JsonCreator
    public ModifierDTO(String name)
    {
        super(name);
    }
}
