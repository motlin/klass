package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ServiceOrderByList extends ServiceOrderByListAbstract
{
    public ServiceOrderByList()
    {
    }

    public ServiceOrderByList(int initialSize)
    {
        super(initialSize);
    }

    public ServiceOrderByList(Collection c)
    {
        super(c);
    }

    public ServiceOrderByList(Operation operation)
    {
        super(operation);
    }
}
