package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OrCritieraList extends OrCritieraListAbstract
{
    public OrCritieraList()
    {
    }

    public OrCritieraList(int initialSize)
    {
        super(initialSize);
    }

    public OrCritieraList(Collection c)
    {
        super(c);
    }

    public OrCritieraList(Operation operation)
    {
        super(operation);
    }
}
