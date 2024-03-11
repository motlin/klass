package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassifierList extends ClassifierListAbstract
{
    public ClassifierList()
    {
    }

    public ClassifierList(int initialSize)
    {
        super(initialSize);
    }

    public ClassifierList(Collection c)
    {
        super(c);
    }

    public ClassifierList(Operation operation)
    {
        super(operation);
    }
}
