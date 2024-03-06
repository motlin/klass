package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AllCriteriaList extends AllCriteriaListAbstract
{
    public AllCriteriaList()
    {
    }

    public AllCriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public AllCriteriaList(Collection c)
    {
        super(c);
    }

    public AllCriteriaList(Operation operation)
    {
        super(operation);
    }
}
