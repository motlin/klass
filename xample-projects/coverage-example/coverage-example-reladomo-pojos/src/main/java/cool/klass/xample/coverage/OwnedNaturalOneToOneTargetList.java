package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedNaturalOneToOneTargetList extends OwnedNaturalOneToOneTargetListAbstract
{
    public OwnedNaturalOneToOneTargetList()
    {
    }

    public OwnedNaturalOneToOneTargetList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedNaturalOneToOneTargetList(Collection c)
    {
        super(c);
    }

    public OwnedNaturalOneToOneTargetList(Operation operation)
    {
        super(operation);
    }
}
