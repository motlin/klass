package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OrderingList extends OrderingListAbstract
{
    public OrderingList()
    {
    }

    public OrderingList(int initialSize)
    {
        super(initialSize);
    }

    public OrderingList(Collection c)
    {
        super(c);
    }

    public OrderingList(Operation operation)
    {
        super(operation);
    }
}
