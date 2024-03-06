package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AssociationEndOrderingList extends AssociationEndOrderingListAbstract
{
    public AssociationEndOrderingList()
    {
    }

    public AssociationEndOrderingList(int initialSize)
    {
        super(initialSize);
    }

    public AssociationEndOrderingList(Collection<?> c)
    {
        super(c);
    }

    public AssociationEndOrderingList(Operation operation)
    {
        super(operation);
    }
}
