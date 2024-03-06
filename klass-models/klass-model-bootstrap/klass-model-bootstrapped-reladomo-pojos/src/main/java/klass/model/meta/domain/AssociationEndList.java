package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AssociationEndList extends AssociationEndListAbstract
{
    public AssociationEndList()
    {
    }

    public AssociationEndList(int initialSize)
    {
        super(initialSize);
    }

    public AssociationEndList(Collection c)
    {
        super(c);
    }

    public AssociationEndList(Operation operation)
    {
        super(operation);
    }
}
