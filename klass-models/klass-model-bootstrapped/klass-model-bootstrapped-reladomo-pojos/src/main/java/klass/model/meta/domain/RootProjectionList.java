package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class RootProjectionList
        extends RootProjectionListAbstract
{
    public RootProjectionList()
    {
    }

    public RootProjectionList(int initialSize)
    {
        super(initialSize);
    }

    public RootProjectionList(Collection c)
    {
        super(c);
    }

    public RootProjectionList(Operation operation)
    {
        super(operation);
    }
}
