package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class OwnedSyntheticOneToOneTargetList extends OwnedSyntheticOneToOneTargetListAbstract
{
    public OwnedSyntheticOneToOneTargetList()
    {
    }

    public OwnedSyntheticOneToOneTargetList(int initialSize)
    {
        super(initialSize);
    }

    public OwnedSyntheticOneToOneTargetList(Collection c)
    {
        super(c);
    }

    public OwnedSyntheticOneToOneTargetList(Operation operation)
    {
        super(operation);
    }
}
