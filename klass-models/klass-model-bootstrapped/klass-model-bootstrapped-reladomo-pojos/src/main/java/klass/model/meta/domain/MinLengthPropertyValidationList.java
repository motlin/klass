package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class MinLengthPropertyValidationList extends MinLengthPropertyValidationListAbstract
{
    public MinLengthPropertyValidationList()
    {
    }

    public MinLengthPropertyValidationList(int initialSize)
    {
        super(initialSize);
    }

    public MinLengthPropertyValidationList(Collection c)
    {
        super(c);
    }

    public MinLengthPropertyValidationList(Operation operation)
    {
        super(operation);
    }
}
