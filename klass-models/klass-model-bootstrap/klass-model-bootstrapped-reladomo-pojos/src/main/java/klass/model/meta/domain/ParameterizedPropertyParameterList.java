package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ParameterizedPropertyParameterList extends ParameterizedPropertyParameterListAbstract
{
    public ParameterizedPropertyParameterList()
    {
    }

    public ParameterizedPropertyParameterList(int initialSize)
    {
        super(initialSize);
    }

    public ParameterizedPropertyParameterList(Collection c)
    {
        super(c);
    }

    public ParameterizedPropertyParameterList(Operation operation)
    {
        super(operation);
    }
}
