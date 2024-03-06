package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalOneToOneSourceList extends SharedNaturalOneToOneSourceListAbstract
{
    public SharedNaturalOneToOneSourceList()
    {
    }

    public SharedNaturalOneToOneSourceList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalOneToOneSourceList(Collection c)
    {
        super(c);
    }

    public SharedNaturalOneToOneSourceList(Operation operation)
    {
        super(operation);
    }
}
