package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class InterfaceList extends InterfaceListAbstract
{
    public InterfaceList()
    {
    }

    public InterfaceList(int initialSize)
    {
        super(initialSize);
    }

    public InterfaceList(Collection c)
    {
        super(c);
    }

    public InterfaceList(Operation operation)
    {
        super(operation);
    }
}
