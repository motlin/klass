package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalOneToOneTargetList extends SharedNaturalOneToOneTargetListAbstract
{
    public SharedNaturalOneToOneTargetList()
    {
    }

    public SharedNaturalOneToOneTargetList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalOneToOneTargetList(Collection c)
    {
        super(c);
    }

    public SharedNaturalOneToOneTargetList(Operation operation)
    {
        super(operation);
    }
}
