package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class ThisMemberReferencePathDTO
        extends MemberReferencePathDTO
{
    public ThisMemberReferencePathDTO(
            ReferenceDTO klass,
            ImmutableList<ReferenceDTO> associationEnds,
            ReferenceDTO dataTypeProperty)
    {
        super(klass, associationEnds, dataTypeProperty);
    }
}
