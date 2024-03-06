package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PrimitivePropertyList extends PrimitivePropertyListAbstract
{
    public PrimitivePropertyList()
    {
    }

    public PrimitivePropertyList(int initialSize)
    {
        super(initialSize);
    }

    public PrimitivePropertyList(Collection c)
    {
        super(c);
    }

    public PrimitivePropertyList(Operation operation)
    {
        super(operation);
    }
}
