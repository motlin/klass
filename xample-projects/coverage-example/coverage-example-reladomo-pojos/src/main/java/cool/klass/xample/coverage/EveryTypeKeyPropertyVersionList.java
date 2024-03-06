package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class EveryTypeKeyPropertyVersionList extends EveryTypeKeyPropertyVersionListAbstract
{
    public EveryTypeKeyPropertyVersionList()
    {
    }

    public EveryTypeKeyPropertyVersionList(int initialSize)
    {
        super(initialSize);
    }

    public EveryTypeKeyPropertyVersionList(Collection c)
    {
        super(c);
    }

    public EveryTypeKeyPropertyVersionList(Operation operation)
    {
        super(operation);
    }
}
