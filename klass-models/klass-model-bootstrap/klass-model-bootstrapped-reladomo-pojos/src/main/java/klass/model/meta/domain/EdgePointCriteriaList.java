package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EdgePointCriteriaList extends EdgePointCriteriaListAbstract
{
    public EdgePointCriteriaList()
    {
    }

    public EdgePointCriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public EdgePointCriteriaList(Collection c)
    {
        super(c);
    }

    public EdgePointCriteriaList(Operation operation)
    {
        super(operation);
    }
}
