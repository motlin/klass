package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class AssociationDTO
        extends PackageableElementDTO
{
    private final ImmutableList<AssociationEndDTO> associationEnds;
    private final CriteriaDTO                      criteria;

    public AssociationDTO(
            String name,
            String packageName,
            ImmutableList<AssociationEndDTO> associationEnds,
            CriteriaDTO criteria)
    {
        super(name, packageName);
        this.associationEnds = associationEnds;
        this.criteria        = criteria;
    }

    public ImmutableList<AssociationEndDTO> getAssociationEnds()
    {
        return this.associationEnds;
    }

    public CriteriaDTO getCriteria()
    {
        return this.criteria;
    }
}
