package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class AndCriteriaList extends AndCriteriaListAbstract
{
    public AndCriteriaList()
    {
    }

    public AndCriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public AndCriteriaList(Collection c)
    {
        super(c);
    }

    public AndCriteriaList(Operation operation)
    {
        super(operation);
    }
}
