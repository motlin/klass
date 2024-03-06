package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class MinPropertyValidationList extends MinPropertyValidationListAbstract
{
    public MinPropertyValidationList()
    {
    }

    public MinPropertyValidationList(int initialSize)
    {
        super(initialSize);
    }

    public MinPropertyValidationList(Collection c)
    {
        super(c);
    }

    public MinPropertyValidationList(Operation operation)
    {
        super(operation);
    }
}
