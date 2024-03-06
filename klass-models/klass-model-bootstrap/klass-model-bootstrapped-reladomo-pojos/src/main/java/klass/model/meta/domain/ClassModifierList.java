package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassModifierList extends ClassModifierListAbstract
{
    public ClassModifierList()
    {
    }

    public ClassModifierList(int initialSize)
    {
        super(initialSize);
    }

    public ClassModifierList(Collection<?> c)
    {
        super(c);
    }

    public ClassModifierList(Operation operation)
    {
        super(operation);
    }
}
