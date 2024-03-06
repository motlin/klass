package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class AssociationEndDTO
{
    private final String                     name;
    private final MultiplicityDTO            multiplicity;
    private final ReferenceDTO               resultType;
    private final ImmutableList<ModifierDTO> modifiers;
    private final ImmutableList<OrderByDTO>  orderBys;

    public AssociationEndDTO(
            String name,
            MultiplicityDTO multiplicity,
            ReferenceDTO resultType,
            ImmutableList<ModifierDTO> modifiers,
            ImmutableList<OrderByDTO> orderBys)
    {
        this.name                    = name;
        this.multiplicity            = multiplicity;
        this.resultType = resultType;
        this.modifiers  = modifiers;
        this.orderBys   = orderBys;
    }

    public String getName()
    {
        return this.name;
    }

    public MultiplicityDTO getMultiplicity()
    {
        return this.multiplicity;
    }

    public ReferenceDTO getResultType()
    {
        return this.resultType;
    }

    public ImmutableList<ModifierDTO> getModifiers()
    {
        return this.modifiers;
    }

    public ImmutableList<OrderByDTO> getOrderBys()
    {
        return this.orderBys;
    }
}
