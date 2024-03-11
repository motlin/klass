package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class MemberReferencePathAssociationEndMappingList extends MemberReferencePathAssociationEndMappingListAbstract
{
    public MemberReferencePathAssociationEndMappingList()
    {
    }

    public MemberReferencePathAssociationEndMappingList(int initialSize)
    {
        super(initialSize);
    }

    public MemberReferencePathAssociationEndMappingList(Collection c)
    {
        super(c);
    }

    public MemberReferencePathAssociationEndMappingList(Operation operation)
    {
        super(operation);
    }
}
