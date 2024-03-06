package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalOneToManySourceList extends SharedNaturalOneToManySourceListAbstract
{
    public SharedNaturalOneToManySourceList()
    {
    }

    public SharedNaturalOneToManySourceList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalOneToManySourceList(Collection c)
    {
        super(c);
    }

    public SharedNaturalOneToManySourceList(Operation operation)
    {
        super(operation);
    }
}
