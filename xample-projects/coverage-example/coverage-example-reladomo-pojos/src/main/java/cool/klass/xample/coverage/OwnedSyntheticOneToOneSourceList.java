package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedSyntheticOneToOneSourceList extends OwnedSyntheticOneToOneSourceListAbstract
{
    public OwnedSyntheticOneToOneSourceList()
    {
    }

    public OwnedSyntheticOneToOneSourceList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedSyntheticOneToOneSourceList(Collection c)
    {
        super(c);
    }

    public OwnedSyntheticOneToOneSourceList(Operation operation)
    {
        super(operation);
    }
}
