package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class DataTypePropertyList extends DataTypePropertyListAbstract
{
    public DataTypePropertyList()
    {
    }

    public DataTypePropertyList(int initialSize)
    {
        super(initialSize);
    }

    public DataTypePropertyList(Collection c)
    {
        super(c);
    }

    public DataTypePropertyList(Operation operation)
    {
        super(operation);
    }
}
