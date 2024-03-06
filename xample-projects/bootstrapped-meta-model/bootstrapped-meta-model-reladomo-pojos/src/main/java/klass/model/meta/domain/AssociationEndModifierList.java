package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AssociationEndModifierList extends AssociationEndModifierListAbstract
{
    public AssociationEndModifierList()
    {
    }

    public AssociationEndModifierList(int initialSize)
    {
        super(initialSize);
    }

    public AssociationEndModifierList(Collection c)
    {
        super(c);
    }

    public AssociationEndModifierList(Operation operation)
    {
        super(operation);
    }
}
