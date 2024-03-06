package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OrderingPartList extends OrderingPartListAbstract
{
    public OrderingPartList()
    {
    }

    public OrderingPartList(int initialSize)
    {
        super(initialSize);
    }

    public OrderingPartList(Collection<?> c)
    {
        super(c);
    }

    public OrderingPartList(Operation operation)
    {
        super(operation);
    }
}
