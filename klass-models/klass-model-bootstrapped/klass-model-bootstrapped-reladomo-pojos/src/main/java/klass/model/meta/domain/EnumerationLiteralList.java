package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EnumerationLiteralList extends EnumerationLiteralListAbstract
{
    public EnumerationLiteralList()
    {
    }

    public EnumerationLiteralList(int initialSize)
    {
        super(initialSize);
    }

    public EnumerationLiteralList(Collection<?> c)
    {
        super(c);
    }

    public EnumerationLiteralList(Operation operation)
    {
        super(operation);
    }
}
