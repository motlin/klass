package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class BinaryCriteriaList extends BinaryCriteriaListAbstract
{
    public BinaryCriteriaList()
    {
    }

    public BinaryCriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public BinaryCriteriaList(Collection c)
    {
        super(c);
    }

    public BinaryCriteriaList(Operation operation)
    {
        super(operation);
    }
}
