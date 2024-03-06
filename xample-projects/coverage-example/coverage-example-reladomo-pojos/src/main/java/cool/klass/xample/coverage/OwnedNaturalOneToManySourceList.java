package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedNaturalOneToManySourceList extends OwnedNaturalOneToManySourceListAbstract
{
    public OwnedNaturalOneToManySourceList()
    {
    }

    public OwnedNaturalOneToManySourceList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedNaturalOneToManySourceList(Collection c)
    {
        super(c);
    }

    public OwnedNaturalOneToManySourceList(Operation operation)
    {
        super(operation);
    }
}
