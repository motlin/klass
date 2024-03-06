package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OrCriteriaList extends OrCriteriaListAbstract
{
    public OrCriteriaList()
    {
    }

    public OrCriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public OrCriteriaList(Collection c)
    {
        super(c);
    }

    public OrCriteriaList(Operation operation)
    {
        super(operation);
    }
}
