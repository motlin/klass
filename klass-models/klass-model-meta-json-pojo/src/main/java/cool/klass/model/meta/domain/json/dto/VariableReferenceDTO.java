package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class VariableReferenceDTO
        implements ExpressionValueDTO
{
    private final ReferenceDTO parameter;

    @JsonCreator
    public VariableReferenceDTO(ReferenceDTO parameter)
    {
        this.parameter = parameter;
    }

    public ReferenceDTO getParameter()
    {
        return this.parameter;
    }
}
