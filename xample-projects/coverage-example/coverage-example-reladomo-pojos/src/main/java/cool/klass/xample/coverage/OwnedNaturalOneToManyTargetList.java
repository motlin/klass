package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedNaturalOneToManyTargetList extends OwnedNaturalOneToManyTargetListAbstract
{
    public OwnedNaturalOneToManyTargetList()
    {
    }

    public OwnedNaturalOneToManyTargetList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedNaturalOneToManyTargetList(Collection c)
    {
        super(c);
    }

    public OwnedNaturalOneToManyTargetList(Operation operation)
    {
        super(operation);
    }
}
