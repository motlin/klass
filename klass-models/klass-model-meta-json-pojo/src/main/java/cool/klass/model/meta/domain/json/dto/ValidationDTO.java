package cool.klass.model.meta.domain.json.dto;

public class ValidationDTO
{
    private final Long number;

    public ValidationDTO(Long number)
    {
        this.number = number;
    }

    public Long getNumber()
    {
        return this.number;
    }
}
