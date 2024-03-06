package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PropertyModifierList extends PropertyModifierListAbstract
{
    public PropertyModifierList()
    {
    }

    public PropertyModifierList(int initialSize)
    {
        super(initialSize);
    }

    public PropertyModifierList(Collection c)
    {
        super(c);
    }

    public PropertyModifierList(Operation operation)
    {
        super(operation);
    }
}
