package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class NullLiteralList extends NullLiteralListAbstract
{
    public NullLiteralList()
    {
    }

    public NullLiteralList(int initialSize)
    {
        super(initialSize);
    }

    public NullLiteralList(Collection c)
    {
        super(c);
    }

    public NullLiteralList(Operation operation)
    {
        super(operation);
    }
}
