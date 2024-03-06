package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EnumerationParameterList extends EnumerationParameterListAbstract
{
    public EnumerationParameterList()
    {
    }

    public EnumerationParameterList(int initialSize)
    {
        super(initialSize);
    }

    public EnumerationParameterList(Collection c)
    {
        super(c);
    }

    public EnumerationParameterList(Operation operation)
    {
        super(operation);
    }
}
