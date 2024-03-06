package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AndCritieraList extends AndCritieraListAbstract
{
    public AndCritieraList()
    {
    }

    public AndCritieraList(int initialSize)
    {
        super(initialSize);
    }

    public AndCritieraList(Collection c)
    {
        super(c);
    }

    public AndCritieraList(Operation operation)
    {
        super(operation);
    }
}
