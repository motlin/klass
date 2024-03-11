package klass.model.meta.domain;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class NamedProjectionList
        extends NamedProjectionListAbstract
{
    public NamedProjectionList()
    {
    }

    public NamedProjectionList(int initialSize)
    {
        super(initialSize);
    }

    public NamedProjectionList(Collection c)
    {
        super(c);
    }

    public NamedProjectionList(Operation operation)
    {
        super(operation);
    }
}
