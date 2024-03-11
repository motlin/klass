package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class MaxLengthPropertyValidationList extends MaxLengthPropertyValidationListAbstract
{
    public MaxLengthPropertyValidationList()
    {
    }

    public MaxLengthPropertyValidationList(int initialSize)
    {
        super(initialSize);
    }

    public MaxLengthPropertyValidationList(Collection c)
    {
        super(c);
    }

    public MaxLengthPropertyValidationList(Operation operation)
    {
        super(operation);
    }
}
