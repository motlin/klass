package cool.klass.model.meta.domain.json.dto;

public class OrCriteriaDTO
        extends BinaryCriteriaDTO
{
    public OrCriteriaDTO(CriteriaDTO left, CriteriaDTO right)
    {
        super(left, right);
    }
}
