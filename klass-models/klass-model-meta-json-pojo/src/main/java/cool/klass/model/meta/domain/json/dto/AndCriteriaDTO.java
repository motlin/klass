package cool.klass.model.meta.domain.json.dto;

public class AndCriteriaDTO
        extends BinaryCriteriaDTO
{
    public AndCriteriaDTO(CriteriaDTO left, CriteriaDTO right)
    {
        super(left, right);
    }
}
