package cool.klass.xample.coverage;

import java.util.Collection;

import com.gs.fw.finder.Operation;

public class SharedNaturalOneToManyTargetList extends SharedNaturalOneToManyTargetListAbstract
{
    public SharedNaturalOneToManyTargetList()
    {
    }

    public SharedNaturalOneToManyTargetList(int initialSize)
    {
        super(initialSize);
    }

    public SharedNaturalOneToManyTargetList(Collection c)
    {
        super(c);
    }

    public SharedNaturalOneToManyTargetList(Operation operation)
    {
        super(operation);
    }
}
