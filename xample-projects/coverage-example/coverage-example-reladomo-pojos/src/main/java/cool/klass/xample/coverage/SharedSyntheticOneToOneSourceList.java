package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedSyntheticOneToOneSourceList extends SharedSyntheticOneToOneSourceListAbstract
{
    public SharedSyntheticOneToOneSourceList()
    {
    }

    public SharedSyntheticOneToOneSourceList(int initialSize)
    {
        super(initialSize);
    }

    public SharedSyntheticOneToOneSourceList(Collection c)
    {
        super(c);
    }

    public SharedSyntheticOneToOneSourceList(Operation operation)
    {
        super(operation);
    }
}
