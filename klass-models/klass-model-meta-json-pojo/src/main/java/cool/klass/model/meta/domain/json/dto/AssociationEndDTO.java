package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class AssociationEndDTO
{
    private final String                     name;
    private final String                     multiplicity;
    private final ReferenceDTO               resultType;
    private final ImmutableList<ModifierDTO> associationEndModifiers;
    private final ImmutableList<OrderByDTO>  orderBys;

    public AssociationEndDTO(
            String name,
            String multiplicity,
            ReferenceDTO resultType,
            ImmutableList<ModifierDTO> associationEndModifiers,
            ImmutableList<OrderByDTO> orderBys)
    {
        this.name                    = name;
        this.multiplicity            = multiplicity;
        this.resultType              = resultType;
        this.associationEndModifiers = associationEndModifiers;
        this.orderBys                = orderBys;
    }

    public String getName()
    {
        return this.name;
    }

    public String getMultiplicity()
    {
        return this.multiplicity;
    }

    public ReferenceDTO getResultType()
    {
        return this.resultType;
    }

    public ImmutableList<ModifierDTO> getAssociationEndModifiers()
    {
        return this.associationEndModifiers;
    }

    public ImmutableList<OrderByDTO> getOrderBys()
    {
        return this.orderBys;
    }
}
