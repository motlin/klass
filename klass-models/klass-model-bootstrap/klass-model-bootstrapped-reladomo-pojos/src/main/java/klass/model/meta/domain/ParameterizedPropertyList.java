package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ParameterizedPropertyList extends ParameterizedPropertyListAbstract
{
    public ParameterizedPropertyList()
    {
    }

    public ParameterizedPropertyList(int initialSize)
    {
        super(initialSize);
    }

    public ParameterizedPropertyList(Collection c)
    {
        super(c);
    }

    public ParameterizedPropertyList(Operation operation)
    {
        super(operation);
    }
}
