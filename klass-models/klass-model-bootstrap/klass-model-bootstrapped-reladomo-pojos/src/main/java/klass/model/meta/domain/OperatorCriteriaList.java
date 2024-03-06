package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OperatorCriteriaList extends OperatorCriteriaListAbstract
{
    public OperatorCriteriaList()
    {
    }

    public OperatorCriteriaList(int initialSize)
    {
        super(initialSize);
    }

    public OperatorCriteriaList(Collection c)
    {
        super(c);
    }

    public OperatorCriteriaList(Operation operation)
    {
        super(operation);
    }
}
