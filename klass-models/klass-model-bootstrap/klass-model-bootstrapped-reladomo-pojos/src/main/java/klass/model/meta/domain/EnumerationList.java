package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EnumerationList extends EnumerationListAbstract
{
    public EnumerationList()
    {
    }

    public EnumerationList(int initialSize)
    {
        super(initialSize);
    }

    public EnumerationList(Collection<?> c)
    {
        super(c);
    }

    public EnumerationList(Operation operation)
    {
        super(operation);
    }
}
