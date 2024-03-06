package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ValidationDTO
{
    private final Long number;

    @JsonCreator
    public ValidationDTO(Long number)
    {
        this.number = number;
    }

    public Long getNumber()
    {
        return this.number;
    }
}
