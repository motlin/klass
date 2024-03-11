package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ServiceList extends ServiceListAbstract
{
    public ServiceList()
    {
    }

    public ServiceList(int initialSize)
    {
        super(initialSize);
    }

    public ServiceList(Collection<?> c)
    {
        super(c);
    }

    public ServiceList(Operation operation)
    {
        super(operation);
    }
}
