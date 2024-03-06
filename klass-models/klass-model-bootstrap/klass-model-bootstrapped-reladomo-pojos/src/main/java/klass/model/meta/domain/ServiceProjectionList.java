package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ServiceProjectionList extends ServiceProjectionListAbstract
{
    public ServiceProjectionList()
    {
    }

    public ServiceProjectionList(int initialSize)
    {
        super(initialSize);
    }

    public ServiceProjectionList(Collection c)
    {
        super(c);
    }

    public ServiceProjectionList(Operation operation)
    {
        super(operation);
    }
}
