package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ClassList extends ClassListAbstract
{
    public ClassList()
    {
    }

    public ClassList(int initialSize)
    {
        super(initialSize);
    }

    public ClassList(Collection c)
    {
        super(c);
    }

    public ClassList(Operation operation)
    {
        super(operation);
    }
}
