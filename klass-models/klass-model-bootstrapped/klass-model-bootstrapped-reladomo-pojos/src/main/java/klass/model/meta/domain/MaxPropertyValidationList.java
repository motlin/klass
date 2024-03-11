package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class MaxPropertyValidationList extends MaxPropertyValidationListAbstract
{
    public MaxPropertyValidationList()
    {
    }

    public MaxPropertyValidationList(int initialSize)
    {
        super(initialSize);
    }

    public MaxPropertyValidationList(Collection c)
    {
        super(c);
    }

    public MaxPropertyValidationList(Operation operation)
    {
        super(operation);
    }
}
