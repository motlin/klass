package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalManyToOneSourceList extends SharedNaturalManyToOneSourceListAbstract
{
    public SharedNaturalManyToOneSourceList()
    {
    }

    public SharedNaturalManyToOneSourceList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalManyToOneSourceList(Collection c)
    {
        super(c);
    }

    public SharedNaturalManyToOneSourceList(Operation operation)
    {
        super(operation);
    }
}
