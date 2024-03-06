package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedSyntheticOneToOneTargetList extends SharedSyntheticOneToOneTargetListAbstract
{
    public SharedSyntheticOneToOneTargetList()
    {
    }

    public SharedSyntheticOneToOneTargetList(int initialSize)
    {
        super(initialSize);
    }

    public SharedSyntheticOneToOneTargetList(Collection c)
    {
        super(c);
    }

    public SharedSyntheticOneToOneTargetList(Operation operation)
    {
        super(operation);
    }
}
