package cool.klass.model.meta.domain.json.dto;

public class OrderByDTO
{
    private final ThisMemberReferencePathDTO thisMemberReferencePath;
    private final OrderByDirectionDTO        orderByDirection;

    public OrderByDTO(
            ThisMemberReferencePathDTO thisMemberReferencePath,
            OrderByDirectionDTO orderByDirection)
    {
        this.thisMemberReferencePath = thisMemberReferencePath;
        this.orderByDirection        = orderByDirection;
    }

    public ThisMemberReferencePathDTO getThisMemberReferencePath()
    {
        return this.thisMemberReferencePath;
    }

    public OrderByDirectionDTO getOrderByDirection()
    {
        return this.orderByDirection;
    }
}
