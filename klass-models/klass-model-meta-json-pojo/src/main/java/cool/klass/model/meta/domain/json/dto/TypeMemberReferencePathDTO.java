package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class TypeMemberReferencePathDTO
        extends MemberReferencePathDTO
{
    public TypeMemberReferencePathDTO(
            ReferenceDTO klass,
            ImmutableList<ReferenceDTO> associationEnds,
            ReferenceDTO dataTypeProperty)
    {
        super(klass, associationEnds, dataTypeProperty);
    }
}
