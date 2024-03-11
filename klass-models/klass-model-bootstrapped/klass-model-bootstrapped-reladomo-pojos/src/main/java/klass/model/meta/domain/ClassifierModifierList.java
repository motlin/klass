package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassifierModifierList extends ClassifierModifierListAbstract
{
    public ClassifierModifierList()
    {
    }

    public ClassifierModifierList(int initialSize)
    {
        super(initialSize);
    }

    public ClassifierModifierList(Collection c)
    {
        super(c);
    }

    public ClassifierModifierList(Operation operation)
    {
        super(operation);
    }
}
