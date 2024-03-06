package cool.klass.model.meta.domain.json.dto;

public abstract class BinaryCriteriaDTO
        implements CriteriaDTO
{
    private final CriteriaDTO left;
    private final CriteriaDTO right;

    protected BinaryCriteriaDTO(CriteriaDTO left, CriteriaDTO right)
    {
        this.left  = left;
        this.right = right;
    }

    public CriteriaDTO getLeft()
    {
        return this.left;
    }

    public CriteriaDTO getRight()
    {
        return this.right;
    }
}
