package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class ParameterizedPropertyOrderingList extends ParameterizedPropertyOrderingListAbstract
{
    public ParameterizedPropertyOrderingList()
    {
    }

    public ParameterizedPropertyOrderingList(int initialSize)
    {
        super(initialSize);
    }

    public ParameterizedPropertyOrderingList(Collection c)
    {
        super(c);
    }

    public ParameterizedPropertyOrderingList(Operation operation)
    {
        super(operation);
    }
}
