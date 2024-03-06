package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public abstract class MemberReferencePathDTO
        implements ExpressionValueDTO
{
    private final ReferenceDTO                klass;
    private final ImmutableList<ReferenceDTO> associationEnds;
    private final ReferenceDTO                dataTypeProperty;

    protected MemberReferencePathDTO(
            ReferenceDTO klass,
            ImmutableList<ReferenceDTO> associationEnds,
            ReferenceDTO dataTypeProperty)
    {
        this.klass            = klass;
        this.associationEnds  = associationEnds;
        this.dataTypeProperty = dataTypeProperty;
    }

    public ReferenceDTO getKlass()
    {
        return this.klass;
    }

    public ImmutableList<ReferenceDTO> getAssociationEnds()
    {
        return this.associationEnds;
    }

    public ReferenceDTO getDataTypeProperty()
    {
        return this.dataTypeProperty;
    }
}
