package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedSyntheticOneToManySourceList extends OwnedSyntheticOneToManySourceListAbstract
{
    public OwnedSyntheticOneToManySourceList()
    {
    }

    public OwnedSyntheticOneToManySourceList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedSyntheticOneToManySourceList(Collection c)
    {
        super(c);
    }

    public OwnedSyntheticOneToManySourceList(Operation operation)
    {
        super(operation);
    }
}
