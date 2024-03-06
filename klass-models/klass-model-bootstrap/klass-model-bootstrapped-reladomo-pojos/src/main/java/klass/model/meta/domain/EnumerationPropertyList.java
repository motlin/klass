package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EnumerationPropertyList extends EnumerationPropertyListAbstract
{
    public EnumerationPropertyList()
    {
    }

    public EnumerationPropertyList(int initialSize)
    {
        super(initialSize);
    }

    public EnumerationPropertyList(Collection c)
    {
        super(c);
    }

    public EnumerationPropertyList(Operation operation)
    {
        super(operation);
    }
}
