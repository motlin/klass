package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class CriteriaList extends CriteriaListAbstract
{
    public CriteriaList()
    {
    }

    public CriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public CriteriaList(Collection c)
    {
        super(c);
    }

    public CriteriaList(Operation operation)
    {
        super(operation);
    }
}
