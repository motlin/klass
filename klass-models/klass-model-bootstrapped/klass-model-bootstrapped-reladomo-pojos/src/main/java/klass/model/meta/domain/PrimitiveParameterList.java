package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class PrimitiveParameterList extends PrimitiveParameterListAbstract
{
    public PrimitiveParameterList()
    {
    }

    public PrimitiveParameterList(int initialSize)
    {
        super(initialSize);
    }

    public PrimitiveParameterList(Collection c)
    {
        super(c);
    }

    public PrimitiveParameterList(Operation operation)
    {
        super(operation);
    }
}
