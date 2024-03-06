package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EveryTypeKeyPropertyList extends EveryTypeKeyPropertyListAbstract
{
    public EveryTypeKeyPropertyList()
    {
    }

    public EveryTypeKeyPropertyList(int initialSize)
    {
        super(initialSize);
    }

    public EveryTypeKeyPropertyList(Collection c)
    {
        super(c);
    }

    public EveryTypeKeyPropertyList(Operation operation)
    {
        super(operation);
    }
}
