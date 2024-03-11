package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ServiceGroupList extends ServiceGroupListAbstract
{
    public ServiceGroupList()
    {
    }

    public ServiceGroupList(int initialSize)
    {
        super(initialSize);
    }

    public ServiceGroupList(Collection<?> c)
    {
        super(c);
    }

    public ServiceGroupList(Operation operation)
    {
        super(operation);
    }
}
