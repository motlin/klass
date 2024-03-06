package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedNaturalOneToOneSourceList extends OwnedNaturalOneToOneSourceListAbstract
{
    public OwnedNaturalOneToOneSourceList()
    {
    }

    public OwnedNaturalOneToOneSourceList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedNaturalOneToOneSourceList(Collection c)
    {
        super(c);
    }

    public OwnedNaturalOneToOneSourceList(Operation operation)
    {
        super(operation);
    }
}
