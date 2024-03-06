package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AssociationEndOrderByList extends AssociationEndOrderByListAbstract
{
    public AssociationEndOrderByList()
    {
    }

    public AssociationEndOrderByList(int initialSize)
    {
        super(initialSize);
    }

    public AssociationEndOrderByList(Collection c)
    {
        super(c);
    }

    public AssociationEndOrderByList(Operation operation)
    {
        super(operation);
    }
}
