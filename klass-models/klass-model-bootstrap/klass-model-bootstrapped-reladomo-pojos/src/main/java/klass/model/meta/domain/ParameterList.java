package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ParameterList extends ParameterListAbstract
{
    public ParameterList()
    {
    }

    public ParameterList(int initialSize)
    {
        super(initialSize);
    }

    public ParameterList(Collection c)
    {
        super(c);
    }

    public ParameterList(Operation operation)
    {
        super(operation);
    }
}
