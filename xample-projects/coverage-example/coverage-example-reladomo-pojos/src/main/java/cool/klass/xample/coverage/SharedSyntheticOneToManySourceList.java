package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedSyntheticOneToManySourceList extends SharedSyntheticOneToManySourceListAbstract
{
    public SharedSyntheticOneToManySourceList()
    {
    }

    public SharedSyntheticOneToManySourceList(int initialSize)
    {
        super(initialSize);
    }

    public SharedSyntheticOneToManySourceList(Collection c)
    {
        super(c);
    }

    public SharedSyntheticOneToManySourceList(Operation operation)
    {
        super(operation);
    }
}
