package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AssociationList extends AssociationListAbstract
{
    public AssociationList()
    {
    }

    public AssociationList(int initialSize)
    {
        super(initialSize);
    }

    public AssociationList(Collection c)
    {
        super(c);
    }

    public AssociationList(Operation operation)
    {
        super(operation);
    }
}
